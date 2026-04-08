extern "C" __global__ void relu_forward(const float* input, float* output, int n) {
    int i = blockIdx.x * blockDim.x + threadIdx.x;
    if (i < n) {
        output[i] = input[i] > 0.0f ? input[i] : 0.0f;
    }
}
