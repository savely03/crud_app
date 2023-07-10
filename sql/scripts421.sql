-- 1 Step
ALTER TABLE student
    ADD CONSTRAINT age_at_least_16 CHECK ( age >= 16 ),
    ALTER COLUMN name SET NOT NULL,
    ADD CONSTRAINT name_unique unique (name),
    ALTER COLUMN age SET DEFAULT 20;

-- 2 Step
ALTER TABLE faculty
    ADD CONSTRAINT name_and_color_unique unique (name, color)