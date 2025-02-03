CREATE TABLE learning_data (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    learning_hours INTEGER DEFAULT 0 NOT NULL,
    recorded_month DATE DEFAULT CURRENT_DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE (user_id, category_id, recorded_month, skill_name), -- ユーザーごとのユニーク制約

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);