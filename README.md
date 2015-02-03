# Epidemic-Simulation-Analysis
Analysis of Epidemic Simulation Time Series data. To Analyze the disease parameters and predict the behavior of disease propagation in the US.

### Phase 1
- Gaussian band discretization of multi dimensional data to capture the onset or peak of disease behaviour.
- Generation of heatmaps using MATLAB to capture disease parameter strength in different states.

### Phase 2
- Performed Eucledian, DTW similarity measures to capture similarity of disease behaviour in different states at different times.
- Performed SVD, LDA and PCA on latent dimensions to identify the important features of multi-dimensional disease parameter data.
- Queried on the latent dimensions to analyze the true similarity of data points.
- Implemented Fast Map algorithm to identify the optimal number of dimensions in the data.

### Phase 3
- Implemented LSH to index the multi-dimensional data and apply optimization techniques to reduce retrieval time.
- Implemented Vector Approximation technique for indexing to reduce the number of lookups in the database.
- Performed Personalized PageRank on latent dimensions of the data.
- Performed Decision Tree Classification to classify the parameters of data to identify measures to be taken.

