extern "C" __global__ void softmax_forward(const float* input, float* output, int n) {
    __shared__ float max_value;
    __shared__ float sum;

    if (threadIdx.x == 0) {
        max_value = input[0];
        for (int i = 1; i < n; ++i) {
            if (input[i] > max_value) {
                max_value = input[i];
            }
        }

        sum = 0.0f;
        for (int i = 0; i < n; ++i) {
            sum += expf(input[i] - max_value);
        }
    }
    __syncthreads();

    int idx = blockIdx.x * blockDim.x + threadIdx.x;
    if (idx < n) {
        output[idx] = expf(input[idx] - max_value) / sum;
    }
}
