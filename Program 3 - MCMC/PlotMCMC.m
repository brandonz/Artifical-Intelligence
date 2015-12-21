M = dlmread('LFData_05');
plot(M(:, 1), M(:, 2))
title('Iteration vs. Probability of 0 Bit');
xlabel('Iterations');
ylabel('Probability of 0 Bit');