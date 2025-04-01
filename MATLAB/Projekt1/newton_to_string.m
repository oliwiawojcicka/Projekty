function polynomial_str = newton_to_string(xi, c)
%Funkcja przyjmuje węzły oraz współczynniki wielomianu danego w postaci
%Newtona i zwraca string z napisanym tym wielomianem (w postaci Newtona)
    n = length(c) - 1; 
    polynomial_str = sprintf('%.0f', c(1)); 

    for i = 2:n+1
        if c(i) == 0
            continue; 
        end
        
        if c(i) > 0
            polynomial_str = [polynomial_str, ' + ']; 
        else
            polynomial_str = [polynomial_str, ' - ']; 
        end
        
        if abs(c(i)) == 1
        else
            polynomial_str = [polynomial_str, sprintf('%.0f', abs(c(i)))]; 
        end
        
        for j = 1:i-1
            if xi(j) == 0
                polynomial_str = [polynomial_str, sprintf('x')]; 
            elseif xi(j) < 0
                polynomial_str = [polynomial_str, sprintf('(x + %.0f)', abs(xi(j)))]; 
            else
                polynomial_str = [polynomial_str, sprintf('(x - %.0f)', xi(j))]; 
            end
        end
    end
end
