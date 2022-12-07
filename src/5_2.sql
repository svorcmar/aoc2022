-- AOC 2022 5-2
-- input: The whole puzzle input string, lines separated with CRLF
CREATE OR REPLACE FUNCTION AOC_2022_5_2(input text) RETURNS TEXT AS $$
  WITH RECURSIVE rows_cte AS
                   (SELECT irow, row_number() over() rn
                      FROM string_to_table(input, E'\r\n') irow),
                 -- splits input rows into base state rows (is_instruction = 0) and step rows (is_instruction = 1),
                 -- removes stack numbers and empty line separator
                 marked_rows_cte AS
                   (SELECT rn, irow, mk AS is_instruction
                      FROM (SELECT rn,
                                   irow,
                                   SUM(CASE irow
                                     WHEN '' THEN
                                       1
                                     ELSE
                                       0
                                       END) over(ORDER BY rn rows unbounded preceding) mk
                              FROM rows_cte) t
                     WHERE irow <> ''
                       AND irow NOT LIKE ' 1 %' /* 1 2 3 4 ...*/
                   ),
                 -- transforms state rows into stack number (1-based index) and stack state - character string with the
                 -- first character being the top of the stack
                 stacks_cte AS
                   (SELECT col + 1 AS stack_no, TRIM(string_agg(letter, '')) stack
                      FROM (SELECT substr(irow, gs + 2, 1) letter, gs / 4 col, rn
                              FROM (SELECT rn, irow, generate_series(0, length(irow), 4) gs
                                      FROM marked_rows_cte
                                     WHERE is_instruction = 0) t1
                             ORDER BY rn) t2
                     GROUP BY col
                     ORDER BY col),
                 -- transforms instruction rows into count, from_stack and to_stack (1-based indices)
                 instructions_cte AS
                   (SELECT rn - MIN(rn) over() + 1 AS step,
                           arr[2]::INT cnt,
                           arr[4]::INT AS i_from,
                           arr[6]::INT AS i_to
                      FROM (SELECT rn, string_to_array(irow, ' ') arr
                              FROM marked_rows_cte
                             WHERE is_instruction = 1) t),
                 -- recursive query:
                 --   end case: 0th step, the stack states are described by stacks_cte
                 --   recursive case: copy the state from the previous step, with the exception of
                 --     - the i_from stack, which is trimmed
                 --     - the i_to stack, which is prefixed with the substring of i_from from the previous step - this
                 --       requires the recursive CTE to be used multiple (two) times, which is not possible and has to
                 --       be worked around by nesting another CTE and using that one twice (see below)
                 stack_steps_cte AS
                   (SELECT 0 ::bigint AS step, stack_no, stack
                      FROM stacks_cte
                     UNION ALL
                    SELECT *
                      FROM ( -- workaround for 'recursive reference to query "stack_steps_cte" must not appear more than once' - nested CTE
                          WITH inner_stacks_cte AS (SELECT *
                                                      FROM stack_steps_cte)
                        SELECT i.step,
                               s.stack_no,
                               CASE
                                 WHEN i.i_from = s.stack_no THEN
                                   substr(s.stack, i.cnt + 1)
                                 WHEN i.i_to = s.stack_no THEN
                                     (SELECT substr(prev.stack, 1, i.cnt)
                                        FROM inner_stacks_cte prev
                                       WHERE prev.stack_no = i.i_from
                                         AND prev.step = i.step - 1) || s.stack
                                 ELSE
                                   s.stack
                               END
                          FROM inner_stacks_cte s
                          JOIN instructions_cte i
                               ON s.step + 1 = i.step) t
                   )
SELECT string_agg(substr(stack, 1, 1), '')
  FROM (SELECT stack
          FROM stack_steps_cte
         WHERE step = (SELECT MAX(step)
                         FROM instructions_cte)
         ORDER BY stack_no) t
$$ LANGUAGE SQL;