-- AOC 2022 3-1
-- input: The whole puzzle input string, lines separated with CRLF
CREATE OR REPLACE FUNCTION AOC_2022_3_1(input text) RETURNS INT AS $$
  WITH rows_cte AS
         (SELECT unnest(string_to_array(input, E'\r\n')) single_row),
       compartments_cte AS
         (SELECT regexp_split_to_array(substr(single_row, 0, length(single_row) / 2 + 1),
                                       '') comp1,
                 regexp_split_to_array(substr(single_row,
                                              length(single_row) / 2 + 1,
                                              length(single_row)),
                                       '') comp2
            FROM rows_cte),
       errors_cte AS
         (SELECT (SELECT *
                    FROM unnest(comp1) AS error
                   WHERE error = ANY(comp2) LIMIT 1)
            FROM compartments_cte)
SELECT SUM(ascii(lower(error)) - ascii('a') + 1 + CASE
  WHEN error BETWEEN 'a' AND 'z' THEN
    0
  ELSE
    26
  END)
  FROM errors_cte
$$ LANGUAGE SQL;