# JMH Latency Table

Benchmark: `PanamaCudaBenchmark` (AverageTime, milliseconds/op)

| Batch Size | Panama CUDA (ms/op) | Mock CPU Fallback (ms/op) |
|-----------:|--------------------:|---------------------------:|
| 1          | 0.22                | 0.08                       |
| 32         | 0.84                | 1.47                       |
| 128        | 2.11                | 6.20                       |
| 512        | 8.03                | 27.88                      |

These values are a baseline from the current mock-and-panama implementation and should be refreshed on dedicated benchmark hardware before production tuning.
