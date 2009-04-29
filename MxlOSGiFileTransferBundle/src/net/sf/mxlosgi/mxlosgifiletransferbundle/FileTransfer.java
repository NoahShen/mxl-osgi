package net.sf.mxlosgi.mxlosgifiletransferbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.JID;

/**
 * @author noah
 * 
 */
public interface FileTransfer
{
	
	/**
	 * Buffer size between input and output
	 */
	public static final int BUFFER_SIZE = 8192;
	
	/**
	 * return sender or receiver full JID
	 * 
	 * @return
	 */
	public JID getPeerJID();

	/**
	 * file name
	 * 
	 * @return
	 */
	public String getFileName();

	/**
	 * file path
	 * 
	 * @return
	 */
	public String getFilePath();
	
	/**
	 * file size
	 * 
	 * @return
	 */
	public long getFileSize();

	/**
	 * get file transfer status
	 * 
	 * @return
	 */
	public Status getStatus();

	/**
	 * Returns the progress of the file transfer as a number between 0 and
	 * 1.
	 * 
	 * @return Returns the progress of the file transfer as a number
	 *         between 0 and 1.
	 */
	public double getProgress();

	/**
	 * 
	 * @return
	 */
	public long getTransferedSize();
	
	/**
	 * 
	 * @return
	 */
	public String getStreamMethod();

	/**
	 * 
	 */
	public void cancel();

	/**
	 * 
	 * @return
	 */
	public Error getError();

	/**
	 * 
	 * @return
	 */
	public Exception getException();

	/**
	 * 
	 * @author noah
	 *
	 */
	public enum Status
	{
		
		inactive,
		/**
		 * An error occured during the transfer.
		 * 
		 */
		error,

		/**
		 * The connecting status of the file transfer.
		 */
		connecting,

		/**
		 * The peer has refused the file transfer request halting
		 * the file transfer negotiation process.
		 */
		refused,

		/**
		 * The transfer is in progress.
		 * 
		 */
		in_progress,

		/**
		 * The transfer has completed successfully.
		 */
		complete,

		/**
		 * The file transfer was canceled
		 */
		cancelled;
	}
	
	public enum Error
	{
		/**
		 * No error
		 */
		none("No error"),

		/**
		 * The peer did not find any of the provided stream
		 * mechanisms acceptable.
		 */
		not_acceptable("The peer did not find any of the provided stream mechanisms acceptable."),

		/**
		 * The provided file to transfer does not exist or could not
		 * be read.
		 */
		bad_file("The provided file to transfer does not exist or could not be read."),

		/**
		 * The remote user did not respond or the connection timed
		 * out.
		 */
		no_response("The remote user did not respond or the connection timed out."),

		/**
		 * An error occured over the socket connected to send the
		 * file.
		 */
		connection("An error occured over the socket connected to send the file."),

		/**
		 * An error occured while sending or recieving the file
		 */
		stream("An error occured while sending or recieving the file.");
		
		private final String msg;

		private Error(String msg)
		{
			this.msg = msg;
		}

		/**
		 * Returns a String representation of this error.
		 * 
		 * @return Returns a String representation of this error.
		 */
		public String getMessage()
		{
			return msg;
		}

		public String toString()
		{
			return msg;
		}
	}
}