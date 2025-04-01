function X = solve_AX_B(A, B)
    % Rozwiązywanie równania AX = B za pomocą rozkładu Crouta
    [L, U] = rozklad_crouta(A);

    % Rozwiązywanie LY = B (eliminacja w przód)
    n = size(L, 1);
    Y = zeros(n, size(B, 2));
    for i = 1:n
        Y(i, :) = (B(i, :) - L(i, 1:i-1) * Y(1:i-1, :)) / L(i, i);
    end

    % Rozwiązywanie UX = Y (eliminacja wstecz)
    X = zeros(n, size(B, 2));
    for i = n:-1:1
        X(i, :) = Y(i, :) - U(i, i+1:n) * X(i+1:n, :);
    end
end