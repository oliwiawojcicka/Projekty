function halley_comparison()
    % Funkcja pokazuje na przykładach przybliżanie pierwiastka wielomianu za
    % pomocą metod Halley'a i Newtona
    examples = {
        { [-1, 0, 7], [4, -1, 3, -1], 5 }, % Funkcja pokazuje dużą różnicę w liczbie iteracji między dwoma metodami
        { [-1, 0, 7], [4, -1, 3, -1], -5 }, % Ta sama funkcja, ale z innym przybliżeniem pokazująca znaczenie wyboru przybliżenia początkowego
        { [-1, 0, 7], [4, -1, 3, -1], 1000 }, % Ta sama funkcja, pokazująca, że wybierając bardzo duże przybliżenie początkowe, metody zbiegają szybciej
        { [-1, 17, 7], [4, -1, 3, -1], 4 }, % Funkcja pokazująca, że wszystkie metody wyznaczają różne pierwiastki
        { [-2, -1, 0, 14, 16], [2, 0, -1, -21, 0, 47], -5 }, % Funkcja o dużym stopniu
        { [1, 3], [2, 4, 1], 5 } % Funkcja bez żadnego pierwiastka, pokazująca brak zbieżności metod
    };

    max_iter = 1000;  
    tol = 1e-8;  
    
    % Przygotowanie wyników do tabel
    roots_table = {};  % Tabela wyników pierwiastków
    errors_table = {};  % Tabela błędów numerycznych

    for i = 1:length(examples)
        example = examples{i};
        xi = example{1}; 
        c = example{2};  
        x0 = example{3}; 
        
        [newton_root, newton_iter, newton_steps] = newton_method(xi, c, x0, max_iter, tol);
        [halley_root, halley_iter, halley_steps] = halley_method(xi, c, x0, max_iter, tol);
        
        options = optimset('Display', 'off');
        [fzero_root, ~, ~] = fzero(@(x) horner_method(x, xi, c), x0, options);
        
        % Obliczanie błędów numerycznych
        newton_error = abs(fzero_root - newton_root);  % Błąd Newtona
        halley_error = abs(fzero_root - halley_root);  % Błąd Halley'a
        
        % Dodajemy dane do tabeli pierwiastków
        roots_table = [roots_table; {sprintf('Przykład %d', i), ...
                                     newton_root, newton_iter, ...
                                     halley_root, halley_iter, ...
                                     fzero_root}];

        % Dodajemy dane do tabeli błędów
        errors_table = [errors_table; {sprintf('Przykład %d', i), ...
                                       newton_error, halley_error}];

        % Wyświetlenie wykresów porównawczych
        halley_vs_newton_plot(xi, c, x0, max_iter, tol, fzero_root);
    end

    % Wyświetlanie tabeli wyników z pierwiastkami
    fprintf('\nTabela wyników - Pierwiastki:\n');
    fprintf('%-25s %-20s %-20s %-20s %-20s %-20s\n', ...
        'Przykład', 'Pierwiastek Newtona', 'Iteracje Newtona', ...
        'Pierwiastek Halley''a', 'Iteracje Halley''a', 'Pierwiastek Wbudowanej');
    for i = 1:size(roots_table, 1)
        fprintf('%-25s %-20.10f %-20d %-20.10f %-20d %-20.10f\n', roots_table{i, :});
    end

    % Wyświetlanie tabeli błędów numerycznych
    fprintf('\nTabela błędów numerycznych:\n');
    fprintf('%-25s %-20s %-20s\n', 'Przykład', 'Błąd Newtona', 'Błąd Halley''a');
    for i = 1:size(errors_table, 1)
        fprintf('%-25s %-20.10e %-20.10e\n', errors_table{i, :});
    end
end
