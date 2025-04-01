function testowy()
    % Przykłady testowe
    przypadki = {
    struct('A', [2, 1, 0, 0, 0; 1, 2, 0, 0, 0; 0, 0, 2, 1, 0; 0, 0, 1, 2, 0; 0, 0, 0, 0, 1], 'B_AX', [5, 7, 9; 7, 9, 11; 5, 7, 9; 7, 9, 11; 5, 7, 9], 'B_XA', [5, 7, 9, 7, 9; 11, 9, 11, 13, 9; 11, 13, 9, 11, 13]),

    struct('A', [2, 1, 3, 5; 4, 7, 6, 2; 6, 8, 2, 1; 4, 1, 7, 7], 'B_AX', eye(4), 'B_XA', eye(4)),

    struct('A', [1, 2, 3, 4, 5; 2, 4, 6, 8, 10; 3, 6, 9, 12, 15; 4, 8, 12, 16, 20; 5, 10, 15, 20, 25], 'B_AX', [1, 1, 1; 1, 1, 1; 1, 1, 1; 1, 1, 1; 1, 1, 1], 'B_XA', [1, 1, 1, 1, 1; 1, 1, 1, 1, 1; 1, 1, 1, 1, 1]'),

    struct('A', eye(6), 'B_AX', [4, 6, 8; 10, 12, 14; 16, 18, 20; 22, 24, 26; 28, 30, 32; 34, 36, 38], 'B_XA', [4, 10, 16, 6, 12, 18; 8, 14, 20, 10, 16, 22; 12, 18, 24, 14, 20, 26]),

    struct('A', [1e6, 1e5, 1e4, 1e3, 1e2, 1e1; 1e5, 1e6, 1e4, 1e3, 1e2, 1e1; 1e4, 1e4, 1e6, 1e3, 1e2, 1e1; 1e3, 1e3, 1e3, 1e6, 1e2, 1e1; 1e2, 1e2, 1e2, 1e2, 1e6, 1e1; 1e1, 1e1, 1e1, 1e1, 1e1, 1e6], 'B_AX', [2e6, 3e6, 4e6; 3e6, 4e6, 5e6; 4e6, 5e6, 6e6; 5e6, 6e6, 7e6; 6e6, 7e6, 8e6; 7e6, 8e6, 9e6], 'B_XA', [2e6, 3e6, 4e6, 3e6, 4e6, 5e6; 4e6, 5e6, 6e6, 5e6, 6e6, 7e6; 6e6, 7e6, 8e6, 7e6, 8e6, 9e6]),

    struct('A', [3e-10, 1e-9, 1e-8, 1e-7, 1e-6, 1e-5; 1e-9, 1e-8, 1e-7, 1e-6, 1e-5, 1e-4; 1e-8, 1e-7, 1e-6, 1e-5, 1e-4, 1e-3; 1e-7, 1e-6, 1e-5, 1e-4, 1e-3, 1e-2; 1e-6, 1e-5, 1e-4, 1e-3, 1e-2, 1e-1; 1e-5, 1e-4, 1e-3, 1e-2, 1e-1, 1e0], 'B_AX', [1e-8, 2e-8, 3e-8; 2e-8, 3e-8, 4e-8; 3e-8, 4e-8, 5e-8; 4e-8, 5e-8, 6e-8; 5e-8, 6e-8, 7e-8; 6e-8, 7e-8, 8e-8], 'B_XA', [1e-8, 2e-8, 3e-8, 2e-8, 3e-8, 4e-8; 3e-8, 4e-8, 5e-8, 4e-8, 5e-8, 6e-8; 5e-8, 6e-8, 7e-8, 6e-8, 7e-8, 8e-8])
   
    };

    % Iteracja po przypadkach
    for i = 1:length(przypadki)
        przypadek = przypadki{i};
        A = przypadek.A;
        B_AX = przypadek.B_AX;
        B_XA = przypadek.B_XA;

        % Wyznaczanie rozkładu Crouta
        [L, U] = rozklad_crouta(A);

        % Wyświetlanie wyników
        fprintf('\nPrzypadek %d:\n', i);
        disp('Macierz A:');
        disp(A);
        disp('Macierz L:');
        disp(L);
        disp('Macierz U:');
        disp(U);

        % Rozwiązywanie równania AX = B
        disp('Rozwiązujemy AX = B:');
        disp('Macierz B_AX:');
        disp(B_AX);
        try
            X_AX = solve_AX_B(A, B_AX);
            disp('Rozwiązanie AX = B (Crout):');
            disp(X_AX);
        catch ME
            disp('Błąd w Crout:');
            disp(ME.message);
        end

        % Porównanie z funkcją wbudowaną
        try
            X_AX_builtin = A \ B_AX;
            disp('Rozwiązanie AX = B (wbudowana):');
            disp(X_AX_builtin);
        catch ME
            disp('Błąd w wbudowanej metodzie:');
            disp(ME.message);
        end

        % Rozwiązywanie równania XA = B
        disp('Rozwiązujemy XA = B:');
        disp('Macierz B_XA:');
        disp(B_XA);
        try
            X_XA = solve_XA_B(A, B_XA);
            disp('Rozwiązanie XA = B (Crout):');
            disp(X_XA);
        catch ME
            disp('Błąd w Crout:');
            disp(ME.message);
        end

        % Porównanie z funkcją wbudowaną
        try
            X_XA_builtin = B_XA / A;
            disp('Rozwiązanie XA = B (wbudowana):');
            disp(X_XA_builtin);
        catch ME
            disp('Błąd w wbudowanej metodzie:');
            disp(ME.message);
        end
    end
end