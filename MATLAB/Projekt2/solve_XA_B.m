function X = solve_XA_B(A, B)
    % Rozwiązywanie równania XA = B za pomocą rozkładu Crouta
    [L, U] = rozklad_crouta(A'); % Rozkład Crouta dla A^T

    % Rozwiązywanie YL = B (eliminacja w przód dla transpozycji)
    n = size(L, 1);
    Y = zeros(size(B, 1), n);
    for i = 1:n
        Y(:, i) = (B(:, i) - Y(:, 1:i-1) * L(i, 1:i-1)') / L(i, i);
    end

    % Rozwiązywanie YU = X (eliminacja wstecz dla transpozycji)
    X = zeros(size(B, 1), n);
    for i = n:-1:1
        X(:, i) = Y(:, i) - X(:, i+1:n) * U(i, i+1:n)';
    end
end