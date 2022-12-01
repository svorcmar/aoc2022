-- AOC 2022 1-2
-- input: The whole puzzle input string, lines separated with CRLF
CREATE OR REPLACE FUNCTION AOC_2022_1_2(input text) RETURNS INT AS $$
  WITH split_cte AS
         (SELECT TRIM(unnest(string_to_array(input,
                                             E'\r\n'))) token),
       ints_cte AS
         (SELECT CASE
                   WHEN token = '' THEN
                     NULL
                   ELSE
                     token::INT
                 END AS token
            FROM split_cte),
       groups_cte AS
         (SELECT token,
                 SUM(CASE
                   WHEN token IS NULL THEN
                     1
                   ELSE
                     0
                     END) over(rows unbounded preceding) AS group_id
            FROM ints_cte)
SELECT SUM(elf_sum)
  FROM (SELECT SUM(token) elf_sum
          FROM groups_cte
         GROUP BY group_id
         ORDER BY elf_sum DESC
         LIMIT 3) t
$$ LANGUAGE SQL;