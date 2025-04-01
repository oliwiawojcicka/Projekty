% Definiowanie węzłów i współczynników
xi = [-1, 0, 7];  % węzły
c = [4, -1, 3, -1];  % współczynniki w postaci Newtona, reprezentujący P(x) = (x-1)(x-2)(x-3)

% Ustawienia
x0 = -5;  % punkt startowy, (10)
max_iter = 100;  % maksymalna liczba iteracji
tol = 1e-8;  % tolerancja zbieżności

% Obliczenia pierwiastków
[newton_root, newton_iter, newton_steps] = newton_method(xi, c, x0, max_iter, tol);
[halley_root, halley_iter, halley_steps] = halley_method(xi, c, x0, max_iter, tol);

% Wbudowana funkcja MATLAB
options = optimset('Display', 'off');
[fzero_root, fzero_fval, fzero_output] = fzero(@(x) horner_method(x, xi, c), x0, options);

polynomial_str = newton_to_string(xi, c);
fprintf('Wielomian: %s\n', polynomial_str);
% Wyświetlanie wyników
fprintf('Pierwiastek Newtona: %.50f, Iteracje: %d\n', newton_root, newton_iter);
fprintf('Pierwiastek Halley''a: %.50f, Iteracje: %d\n', halley_root, halley_iter);
fprintf('Pierwiastek wbudowanej funkcji MATLAB (fzero): %.50f\n', fzero_root);

% Wykresy porównawcze
halley_vs_newton_plot(xi, c, x0, max_iter, tol, fzero_root);
fprintf('Newton\n')
for i = 1:(length(newton_steps))
    fprintf('%.50f\n', (newton_steps(i)));
end
fprintf('Halley\n')
for i = 1:length(halley_steps)
    fprintf('%.50f\n', halley_steps(i));
end