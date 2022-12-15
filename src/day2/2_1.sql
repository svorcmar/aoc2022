-- AOC 2022 2-1
-- input: The whole puzzle input string, lines separated with CRLF
CREATE OR REPLACE FUNCTION aoc_2022_2_1(input text) RETURNS INT AS $$
  WITH input_rows_cte AS
         (SELECT unnest(string_to_array(input, E'\r\n')) irow),
       input_cte AS
         (SELECT ascii(arr[1]) - ascii('A') p1,
                 ascii(arr[2]) - ascii('X') p2
            FROM (SELECT string_to_array(irow, ' ') arr
                    FROM input_rows_cte) t),
       results_cte AS
         (SELECT p1,
                 p2,
                 (p1 - p2 + 3) % 3 winner
            FROM input_cte)
SELECT SUM(p2 + 1 +
  CASE winner
    WHEN 0 THEN
      3 -- draw
    WHEN 1 THEN
      0 -- loss
    ELSE
      6 -- victory
  END)
  FROM results_cte;
$$
  LANGUAGE sql;