extern "C" __global__ void layernorm_forward(const float* input, float* output, int n, float epsilon) {
    __shared__ float mean;
    __shared__ float variance;

    if (threadIdx.x == 0) {
        mean = 0.0f;
        for (int i = 0; i < n; ++i) {
            mean += input[i];
        }
        mean /= n;

        variance = 0.0f;
        for (int i = 0; i < n; ++i) {
            float centered = input[i] - mean;
            variance += centered * centered;
        }
        variance /= n;
    }
    __syncthreads();

    int idx = blockIdx.x * blockDim.x + threadIdx.x;
    if (idx < n) {
        output[idx] = (input[idx] - mean) / sqrtf(variance + epsilon);
    }
}
