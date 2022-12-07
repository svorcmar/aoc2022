-- AOC 2022 6-2
-- input: The whole puzzle input string, lines separated with CRLF
CREATE OR REPLACE FUNCTION AOC_2022_6_2(input text) RETURNS INT AS $$
  WITH segments_cte AS
         (SELECT idx, substr(input, idx, 14) segment
            FROM (SELECT input, generate_series(1, length(input) - 13) idx) t)
SELECT idx + 13
  FROM (SELECT idx,
               (SELECT count(DISTINCT c)
                  FROM regexp_split_to_table(segment, '') c) unique_chars
          FROM segments_cte) t
 WHERE unique_chars = 14
 ORDER BY idx LIMIT 1
$$ LANGUAGE SQL;
