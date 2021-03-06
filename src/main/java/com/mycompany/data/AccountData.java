package com.mycompany.data;

import org.libx4j.rdb.jsql.RowIterator;
import org.libx4j.rdb.jsql.Transaction;
import org.libx4j.rdb.jsql.mycompany;

import java.io.IOException;
import java.sql.SQLException;

import static org.libx4j.rdb.jsql.DML.*;

public final class AccountData {
  public static mycompany.Account saveAccount(final mycompany.Account a) throws IOException, SQLException {
    if (a.id.get() != null) {
      UPDATE(a).execute();
      return a;
    }

    try (final Transaction transaction = new Transaction(mycompany.class)) {
      final RowIterator<mycompany.Account> rows =
        SELECT(a).
        FROM(a).
        WHERE(EQ(a.email, a.email.get())).
        execute(transaction);

      if (rows.nextRow())
        return null; // Account already exists

      INSERT(a).execute(transaction);
      transaction.commit();
    }

    return a;
  }

  public static mycompany.Account findAccount(final String email) throws IOException, SQLException {
    final mycompany.Account a = new mycompany.Account();
    try (final RowIterator<mycompany.Account> rows =
      SELECT(a).
      FROM(a).
      WHERE(EQ(a.email, email)).
      execute()) {
      return rows.nextRow() ? rows.nextEntity() : null;
    }
  }

  public static int removeAccountToken(final String token) throws IOException, SQLException {
    final mycompany.Account a = new mycompany.Account();
    return UPDATE(a).
      SET(a.forgotToken, (String)null).
      WHERE(EQ(a.forgotToken, token)).execute();
  }

  public static mycompany.Account findAccountByToken(final String token) throws IOException, SQLException {
    final mycompany.Account a = new mycompany.Account();
    try (final RowIterator<mycompany.Account> rows =
      SELECT(a).
      FROM(a).
      WHERE(EQ(a.forgotToken, token)).
      execute()) {
      return rows.nextRow() ? rows.nextEntity() : null;
    }
  }

  public static mycompany.Account findAccount(final String email, final String password) throws IOException, SQLException {
    final mycompany.Account a = new mycompany.Account();
    try (final RowIterator<mycompany.Account> rows =
      SELECT(a).
      FROM(a).
      WHERE(AND(EQ(a.email, email), EQ(a.password, password))).
      execute()) {
      return rows.nextRow() ? rows.nextEntity() : null;
    }
  }

  private AccountData() {
  }
}