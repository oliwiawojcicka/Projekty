function [L, U] = rozklad_crouta(A)
    % Funkcja wyznaczająca rozkład Crouta macierzy A
    % L - macierz dolnotrójkątna
    % U - macierz górnotrójkątna z jedynkami na przekątnej

    % Sprawdzenie wymiarów macierzy
    [n, m] = size(A);
    if n ~= m
        error('Macierz A musi być kwadratowa.');
    end

    % Inicjalizacja macierzy L i U
    L = zeros(n);  % Dolnotrójkątna
    U = eye(n);    % Górnotrójkątna z jedynkami na przekątnej

    % Algorytm Crouta
    for j = 1:n
        % Obliczanie elementów L (kolumna j)
        for i = j:n
            L(i, j) = A(i, j) - sum(L(i, 1:j-1) .* U(1:j-1, j)');
        end
        
        % Obliczanie elementów U (wiersz j)
        for i = j+1:n
            U(j, i) = (A(j, i) - sum(L(j, 1:j-1) .* U(1:j-1, i)')) / L(j, j);
        end
    end
end