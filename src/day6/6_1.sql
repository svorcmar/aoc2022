-- AOC 2022 6-1
-- input: The whole puzzle input string, lines separated with CRLF
CREATE OR REPLACE FUNCTION AOC_2022_6_1(input text) RETURNS INT AS $$
  WITH segments_cte AS
         (SELECT idx, substr(input, idx, 4) segment
            FROM (SELECT input, generate_series(1, length(input) - 3) idx) t)
SELECT idx + 3
  FROM (SELECT idx,
               (SELECT count(DISTINCT c)
                  FROM regexp_split_to_table(segment, '') c) unique_chars
          FROM segments_cte) t
 WHERE unique_chars = 4
 ORDER BY idx LIMIT 1
$$ LANGUAGE SQL;
