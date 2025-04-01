function skrypt_testujacy()
    % Definicje przypadków testowych
    przypadki = {

    struct('A', [2, 1, 0, 0, 0; 1, 2, 0, 0, 0; 0, 0, 2, 1, 0; 0, 0, 1, 2, 0; 0, 0, 0, 0, 1], 'B_AX', [5, 7, 9; 7, 9, 11; 5, 7, 9; 7, 9, 11; 5, 7, 9], 'B_XA', [5, 7, 9, 7, 9; 11, 9, 11, 13, 9; 11, 13, 9, 11, 13]),

    struct('A', [2, 1, 3, 5; 4, 7, 6, 2; 6, 8, 2, 1; 4, 1, 7, 7], 'B_AX', eye(4), 'B_XA', eye(4)),

    struct('A', [1, 2, 3, 4, 5; 2, 4, 6, 8, 10; 3, 6, 9, 12, 15; 4, 8, 12, 16, 20; 5, 10, 15, 20, 25], 'B_AX', [1, 1, 1; 1, 1, 1; 1, 1, 1; 1, 1, 1; 1, 1, 1], 'B_XA', [1, 1, 1, 1, 1; 1, 1, 1, 1, 1; 1, 1, 1, 1, 1]'),

    struct('A', eye(6), 'B_AX', [4, 6, 8; 10, 12, 14; 16, 18, 20; 22, 24, 26; 28, 30, 32; 34, 36, 38], 'B_XA', [4, 10, 16, 6, 12, 18; 8, 14, 20, 10, 16, 22; 12, 18, 24, 14, 20, 26]),

    struct('A', [1e6, 1e5, 1e4, 1e3, 1e2, 1e1; 1e5, 1e6, 1e4, 1e3, 1e2, 1e1; 1e4, 1e4, 1e6, 1e3, 1e2, 1e1; 1e3, 1e3, 1e3, 1e6, 1e2, 1e1; 1e2, 1e2, 1e2, 1e2, 1e6, 1e1; 1e1, 1e1, 1e1, 1e1, 1e1, 1e6], 'B_AX', [2e6, 3e6, 4e6; 3e6, 4e6, 5e6; 4e6, 5e6, 6e6; 5e6, 6e6, 7e6; 6e6, 7e6, 8e6; 7e6, 8e6, 9e6], 'B_XA', [2e6, 3e6, 4e6, 3e6, 4e6, 5e6; 4e6, 5e6, 6e6, 5e6, 6e6, 7e6; 6e6, 7e6, 8e6, 7e6, 8e6, 9e6]),

    struct('A', [3e-10, 1e-9, 1e-8, 1e-7, 1e-6, 1e-5; 1e-9, 1e-8, 1e-7, 1e-6, 1e-5, 1e-4; 1e-8, 1e-7, 1e-6, 1e-5, 1e-4, 1e-3; 1e-7, 1e-6, 1e-5, 1e-4, 1e-3, 1e-2; 1e-6, 1e-5, 1e-4, 1e-3, 1e-2, 1e-1; 1e-5, 1e-4, 1e-3, 1e-2, 1e-1, 1e0], 'B_AX', [1e-8, 2e-8, 3e-8; 2e-8, 3e-8, 4e-8; 3e-8, 4e-8, 5e-8; 4e-8, 5e-8, 6e-8; 5e-8, 6e-8, 7e-8; 6e-8, 7e-8, 8e-8], 'B_XA', [1e-8, 2e-8, 3e-8, 2e-8, 3e-8, 4e-8; 3e-8, 4e-8, 5e-8, 4e-8, 5e-8, 6e-8; 5e-8, 6e-8, 7e-8, 6e-8, 7e-8, 8e-8])
    };

    wyniki = [];
    bledy = [];

    % Przetwarzanie przypadków testowych
    for i = 1:length(przypadki)
        przypadek = przypadki{i};
        A = przypadek.A;
        B_AX = przypadek.B_AX;
        B_XA = przypadek.B_XA;

        % Sprawdzenie kondycji macierzy
        if rcond(A) < 1e-12
            warning('Macierz A w przypadku %d jest bliska osobliwości. Wyniki mogą być niedokładne.', i);
        end

        % Rozwiązywanie równania AX = B
        try
            X_AX_crout = solve_AX_B(A, B_AX);
            X_AX_builtin = A \ B_AX;
        catch
            X_AX_crout = NaN;
            X_AX_builtin = NaN;
        end

        % Rozwiązywanie równania XA = B
        try
            X_XA_crout = solve_XA_B(A, B_XA);
            X_XA_builtin = B_XA / A;
        catch
            X_XA_crout = NaN;
            X_XA_builtin = NaN;
        end

        % Obliczanie błędów
        blad_AX_crout = norm(X_AX_crout - X_AX_builtin, 'fro');
        blad_XA_crout = norm(X_XA_crout - X_XA_builtin, 'fro');
        blad_AX_builtin = 0;
        blad_XA_builtin = 0;

        % Zaokrąglanie macierzy do trzech miejsc po przecinku
        X_AX_crout = round(X_AX_crout, 3);
        X_AX_builtin = round(X_AX_builtin, 3);
        X_XA_crout = round(X_XA_crout, 3);
        X_XA_builtin = round(X_XA_builtin, 3);

        % Dodawanie wyników i błędów do tabeli
        wyniki = [wyniki; {
            i, mat2str(A), mat2str(B_AX), mat2str(B_XA), mat2str(X_AX_crout), mat2str(X_AX_builtin), mat2str(X_XA_crout), mat2str(X_XA_builtin)
        }];
        bledy = [bledy; {i, blad_AX_crout, blad_AX_builtin, blad_XA_crout, blad_XA_builtin}];
    end

    % Tworzenie tabeli z wynikami
    wynik_table = cell2table(wyniki, 'VariableNames', ...
        {'Przypadek', 'A', 'B_AX', 'B_XA', 'X_AX_crout', 'X_AX_builtin', 'X_XA_crout', 'X_XA_builtin'});

    % Wyświetlanie wyników w tabeli
    disp('Tabela wyników:');
    disp(wynik_table);

    % Tworzenie tabeli z błędami
    blad_table = cell2table(bledy, 'VariableNames', ...
        {'Przypadek', 'Blad_AX_Crout', 'Blad_AX_Builtin', 'Blad_XA_Crout', 'Blad_XA_Builtin'});

    % Wyświetlanie tabeli z błędami
    disp('Tabela błędów:');
    disp(blad_table);

    % Tworzenie wykresów
    metoda_glowna(wyniki, bledy);
end

