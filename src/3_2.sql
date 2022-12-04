-- AOC 2022 3-2
-- input: The whole puzzle input string, lines separated with CRLF
CREATE OR REPLACE FUNCTION AOC_2022_3_2(input text) RETURNS INT AS $$
  WITH backpacks_cte AS
         (SELECT backpack, row_number() over() - 1 AS backpack_id
            FROM (SELECT regexp_split_to_array(unnest(string_to_array(input, E'\r\n')), '') backpack) t),
       triples_cte AS
         (SELECT b1.backpack b1, b2.backpack b2, b3.backpack b3
            FROM backpacks_cte b1
            JOIN backpacks_cte b2
                 ON b1.backpack_id = b2.backpack_id - 1
            JOIN backpacks_cte b3
                 ON b2.backpack_id = b3.backpack_id - 1
           WHERE b1.backpack_id % 3 = 0),
       badges_cte AS
         (SELECT (SELECT *
                    FROM unnest(b1) AS badge
                   WHERE badge = ANY(b2)
                     AND badge = ANY(b3) LIMIT 1)
            FROM triples_cte)
SELECT SUM(ascii(lower(badge)) - ascii('a') + 1 + CASE
  WHEN badge BETWEEN 'a' AND 'z' THEN
    0
  ELSE
    26
  END)
  FROM badges_cte
$$ LANGUAGE SQL;