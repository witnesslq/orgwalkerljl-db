package org.walkerljl.db.transaction.jdbc;

import org.walkerljl.commons.log.Logger;
import org.walkerljl.commons.log.LoggerFactory;
import org.walkerljl.db.transaction.Transaction;
import org.walkerljl.db.transaction.TransactionException;
import org.walkerljl.db.transaction.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
**
* {@link Transaction} that makes use of the JDBC commit and rollback facilities directly.
* It relies on the connection retrieved from the dataSource to manage the scope of the transaction.
* Delays connection retrieval until getConnection() is called.
* Ignores commit or rollback requests when autocommit is on.
*
* @see JdbcTransactionFactory
*/
public class JdbcTransaction implements Transaction {
	
	private static final Logger LOG = LoggerFactory.getLogger(JdbcTransaction.class);

	protected Connection connection;
	protected DataSource dataSource;
	protected TransactionIsolationLevel level;
	protected boolean autoCommmit;
	
	public JdbcTransaction(DataSource ds, TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit) {
		dataSource = ds;
		level = desiredLevel;
		autoCommmit = desiredAutoCommit;
	} 
	
	public JdbcTransaction(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		if (connection == null) {
			openConnection();
		}
		return connection;
	}
	
	@Override
	public void commit() throws SQLException {
		if (connection != null && !connection.getAutoCommit()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Committing JDBC Connection [" + connection + "]");
			}
			connection.commit();
		}
	}

	@Override
	public void rollback() throws SQLException {
		if (connection != null && !connection.getAutoCommit()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Rolling back JDBC Connection [" + connection + "]");
			}
			connection.rollback();
		}
	}
	
	@Override
	public void close() throws SQLException {
		if (connection != null) {
			resetAutoCommit();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Closing JDBC Connection [" + connection + "]");
			}
			connection.close();
	   }
	}
	
	protected void setDesiredAutoCommit(boolean desiredAutoCommit) {
		try {
			if (connection.getAutoCommit() != desiredAutoCommit) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Setting autocommit to " + desiredAutoCommit + " on JDBC Connection [" + connection + "]");
				}
				connection.setAutoCommit(desiredAutoCommit);
			}
	   } catch (SQLException e) {
	     // Only a very poorly implemented driver would fail here,
	     // and there's not much we can do about that.
	     throw new TransactionException("Error configuring AutoCommit.  "
	         + "Your driver may not support getAutoCommit() or setAutoCommit(). "
	         + "Requested setting: " + desiredAutoCommit + ".  Cause: " + e, e);
	   }
	 }
	
	protected void resetAutoCommit() {
	   try {
	     if (!connection.getAutoCommit()) {
	       // MyBatis does not call commit/rollback on a connection if just selects were performed.
	       // Some databases start transactions with select statements
	       // and they mandate a commit/rollback before closing the connection.
	       // A workaround is setting the autocommit to true before closing the connection.
	       // Sybase throws an exception here.
	       if (LOG.isDebugEnabled()) {
	    	   LOG.debug("Resetting autocommit to true on JDBC Connection [" + connection + "]");
	       }
	       connection.setAutoCommit(true);
	     }
	   } catch (SQLException e) {
		   LOG.debug("Error resetting autocommit to true "
	         + "before closing the connection.  Cause: " + e);
	   }
	 }
	
	 protected void openConnection() throws SQLException {
		 if (LOG.isDebugEnabled()) {
			 LOG.debug("Opening JDBC Connection");
		 }
		 connection = dataSource.getConnection();
		 if (level != null) {
			 connection.setTransactionIsolation(level.getLevel());
		 }
		 setDesiredAutoCommit(autoCommmit);
	}
}