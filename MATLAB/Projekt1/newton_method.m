function [root, iter, approximations] = newton_method(xi, c, x0, max_iter, tol)
%Funkcja oblicza pierwiastek wielomianu danego w postaci Newtona za pomocą
% metody Newtona
% Podajemy wielomian w postaci Newtona: xi - węzły interpolacyjne,
% c - współczynniki postaci Newtona
    x = x0; 
    approximations = zeros(max_iter+1, 1); % Zapiszemy kolejne przybliżenia
    approximations(1)=x;
    for iter = 1:max_iter
        [fx, dfx, ~] = horner_method(x, xi, c);

        if abs(dfx) < eps
            error('Dzielenie przez 0 w metodzie Newtona.');
        end
        % Wzór metody Newtona
        x_new = x - fx / dfx;

        approximations(iter+1) = x_new;

        % Sprawdzenie warunku zbieżności
        if abs(x_new - x) < tol
            root = x_new; 
            approximations = approximations(1:iter+1); 
            return; 
        end

        x = x_new;
    end

    root = NaN; % Zwracamy NaN jeśli metoda nie zbiega
end
