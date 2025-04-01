function str = mat2str_2d(M)
    [rows, cols] = size(M);
    str = '';
    for i = 1:rows
        str = strcat(str, sprintf('%s\n', num2str(M(i, :), '%.3f')));
    end
end