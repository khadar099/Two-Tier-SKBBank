***When a Database is Needed:***

User registration/login – store usernames, passwords (hashed), etc.

Accounts – maintain account balances, account numbers.

Transactions – record deposits, withdrawals, transfers.

Audit logs – keep track of what users did and when.

Reports – generate statements, summaries, etc.

***When a Database Might Not Be Needed:***

If it’s a purely demo app with no persistence at all.

If you're just testing APIs and don't need to store anything.

If you're using in-memory storage like HashMap or H2 for temporary testing.
