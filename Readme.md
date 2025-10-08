Create User table
```sql
CREATE TABLE [dbo].[User](
[id] [int] NOT NULL,
[name] [varchar](50) NULL,
[email] [varchar](50) NULL,
[phone] [varchar](50) NULL
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[User] ADD PRIMARY KEY CLUSTERED
(
[id] ASC
);
```