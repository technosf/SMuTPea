package com.github.technosf.smutpea.core.rfc2821;

import static java.util.Objects.requireNonNull;

/**
 * Buffer, per RFC2821 Section 2.3.6
 * <p>
 * Email message buffer per RFC2821 Section 2.3.6. Contains the email message, the forward and reverse paths.
 * 
 * @see http://tools.ietf.org/html/rfc2821#section-2.3.6
 * 
 * @author technosf
 * @since 0.0.1
 * @version 0.0.1
 */
public final class Buffer
{

	/**
	 * The email data - the message
	 */
	private StringBuffer	mailData	= new StringBuffer();

	/**
	 * The email forward path
	 */
	private String			forwardPath	= "";

	/**
	 * The email reverse path
	 */
	private String			reversePath	= "";


	/**
	 * Appends to the mail data
	 * 
	 * @param mailData
	 *            the mail data to append
	 * @throws NullPointerException
	 *             mail data cannot be null
	 */
	void appendMailData(String mailData) throws NullPointerException
	{
		this.mailData.append(requireNonNull(mailData)).append(Session.CRLF);
	};


	/**
	 * Sets the forward path
	 * 
	 * @param forwardPath
	 *            the forward path
	 * @throws NullPointerException
	 *             the forward path cannot be null
	 */
	void setForwardPath(String forwardPath) throws NullPointerException
	{
		this.forwardPath = requireNonNull(forwardPath);
	};


	/**
	 * Sets the reverse path
	 * 
	 * @param reversePath
	 *            the reverse path
	 * @throws NullPointerException
	 *             reverse path cannot be null
	 */
	void setReversePath(String reversePath) throws NullPointerException
	{
		this.reversePath = requireNonNull(reversePath);
	};


	/**
	 * Return the mail data
	 * 
	 * @return the mail data
	 */
	String getMailData()
	{
		return mailData.toString().trim();
	};


	/**
	 * Returns the forward path
	 * 
	 * @return the forward path
	 */
	String getForwardPath()
	{
		return forwardPath;
	};


	/**
	 * Returns the reverse path
	 * 
	 * @return the reverse path
	 */
	String getReversePath()
	{
		return reversePath;
	};


	/**
	 * Clear the mail data, forward and reverse paths
	 */
	void clear()
	{
		clearMailData();
		clearForwardPath();
		clearReversePath();
	};


	/**
	 * Clear the mail data
	 */
	void clearMailData()
	{
		mailData = new StringBuffer();
	};


	/**
	 * Clear the forward path
	 */
	void clearForwardPath()
	{
		forwardPath = "";
	};


	/**
	 * Clear the reverse path
	 */
	void clearReversePath()
	{
		reversePath = "";
	};
}
