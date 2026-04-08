package com.ameqran.cuda.inference.interfaces.api;

import com.ameqran.cuda.inference.application.command.RegisterModelCommand;
import com.ameqran.cuda.inference.application.usecase.ListModelsUseCase;
import com.ameqran.cuda.inference.application.usecase.RegisterModelUseCase;
import com.ameqran.cuda.inference.domain.aggregate.ModelFormat;
import com.ameqran.cuda.inference.interfaces.dto.ModelDto;
import com.ameqran.cuda.inference.interfaces.dto.RegisterModelRequest;
import com.ameqran.cuda.inference.interfaces.mapper.ApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/models")
public class ModelController {

    private final RegisterModelUseCase registerModelUseCase;
    private final ListModelsUseCase listModelsUseCase;
    private final ApiMapper mapper = new ApiMapper();

    public ModelController(RegisterModelUseCase registerModelUseCase, ListModelsUseCase listModelsUseCase) {
        this.registerModelUseCase = registerModelUseCase;
        this.listModelsUseCase = listModelsUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModelDto register(@Valid @RequestBody RegisterModelRequest request) {
        ModelFormat format = parseFormat(request.format());
        byte[] onnxBytes = Base64.getDecoder().decode(request.onnxBase64());

        return mapper.toModelDto(registerModelUseCase.handle(
                new RegisterModelCommand(request.name(), format, onnxBytes)
        ));
    }

    @GetMapping
    public List<ModelDto> list(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size) {
        return listModelsUseCase.handle(page, size).stream().map(mapper::toModelDto).toList();
    }

    private ModelFormat parseFormat(String format) {
        String normalized = format.trim().toUpperCase(Locale.ROOT).replace("-", "_");
        if ("TORCHSCRIPT".equals(normalized)) {
            normalized = "TORCH_SCRIPT";
        }
        return ModelFormat.valueOf(normalized);
    }
}
