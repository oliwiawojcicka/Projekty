function halley_vs_newton_plot(xi, c, x0, max_iter, tol, fzero_root)
%Funkcja rysuje wykres porównujący poszczególne przybliżenia pierwiastka
%wielomianu za pomocą metody Newtona i metody Halley'a oraz porównuje je z
%pierwiastkiem obliczonym za pomocą funkcji wbudowanej fzero
    [newton_root, newton_iter, newton_steps] = newton_method(xi, c, x0, max_iter, tol);
    [halley_root, halley_iter, halley_steps] = halley_method(xi, c, x0, max_iter, tol);

    newton_converged = ~(isnan(newton_root));
    halley_converged = ~(isnan(halley_root));
    figure;
    hold on;

    if newton_converged
        plot(0:newton_iter, newton_steps, 'ro-', 'DisplayName', sprintf('Metoda Newtona (Iteracje: %d)', newton_iter));
    else
        plot(0:max_iter, newton_steps, 'r--', 'DisplayName', 'Metoda Newtona (Brak zbieżności)');
    end

    if halley_converged
        plot(0:halley_iter, halley_steps, 'bs-', 'DisplayName', sprintf('Metoda Halley''a (Iteracje: %d)', halley_iter));
    else
        plot(0:max_iter, halley_steps, 'b--', 'DisplayName', 'Metoda Halley''a (Brak zbieżności)');
    end

    if isnan(fzero_root) 
        yline(fzero_root, 'g--', 'DisplayName', 'Funkcja wbudowana(Brak wyniku)');
    else 
        yline(fzero_root, 'g--', 'DisplayName', 'Funkcja wbudowana');
    end

    xlabel('Iteracja');
    ylabel('Przybliżenie pierwiastka');
    legend('show', 'Location', 'best', 'FontSize', 10);
    polynomial_str = newton_to_string(xi, c);
    title_text = sprintf('Porównanie metod dla wielomianu: %s, przybliżenie początkowe: %d', polynomial_str, x0);
    title(title_text, 'FontSize', 14, 'FontWeight', 'bold');

    hold off;
end


