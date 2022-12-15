-- AOC 2022 4-1
-- input: The whole puzzle input string, lines separated with CRLF
CREATE OR REPLACE FUNCTION AOC_2022_4_1(input text) RETURNS INT AS $$
  WITH input_rows_cte AS
         (SELECT unnest(string_to_array(input, E'\r\n')) irow),
       elf_ranges_cte AS
         (SELECT t3.e1range[1]::INT e1start,
                 t3.e1range[2]::INT e1end,
                 t3.e2range[1]::INT e2start,
                 t3.e2range[2]::INT e2end
            FROM (SELECT string_to_array(t2.elf1, '-') e1range,
                         string_to_array(t2.elf2, '-') e2range
                    FROM (SELECT t.ranges[1] elf1, t.ranges[2] elf2
                            FROM (SELECT string_to_array(irow, ',') ranges
                                    FROM input_rows_cte) t) t2) t3)
SELECT COUNT(1)
  FROM elf_ranges_cte e
 WHERE e1start BETWEEN e2start AND e2end
   AND e1end BETWEEN e2start AND e2end
    OR e2start BETWEEN e1start AND e1end
   AND e2end BETWEEN e1start AND e1end
$$ LANGUAGE SQL;