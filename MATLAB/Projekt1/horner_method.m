function [w_a, dw_a, d2w_a] = horner_method(a, x, c)
%Funkcja oblicza wartość wielomianu oraz jego pierwszej i drugiej pochodnej
%w punkcie a
% Podajemy wielomian w postaci Newtona: x - węzły interpolacyjne,
% c - współczynniki postaci Newtona
    n = length(c) - 1; 
    if length(x) ~= n
        error('Liczba węzłów interpolacyjnych (x) musi być równa n.');
    end
    w_a = c(n+1);
    dw_a = 0;    
    d2w_a = 0;   

    for i = n:-1:1
        d2w_a = d2w_a * (a - x(i)) + 2 * dw_a;
        dw_a = dw_a * (a - x(i)) + w_a;
        w_a = w_a * (a - x(i)) + c(i);
    end
end

