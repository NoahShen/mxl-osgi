package net.sf.mxlosgi.mxlosgibookmarksbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.XMLStanza;

/**
 * 
 * @author noah
 *
 */
public class BookmarkedURL implements XMLStanza
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4881225822426395251L;
	
	private String name;

	private String url;

	/**
	 * @param url
	 */
	public BookmarkedURL(String url)
	{
		this.url = url;
	}

	/**
	 * @param name
	 * @param url
	 */
	public BookmarkedURL(String name, String url)
	{
		this.name = name;
		this.url = url;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	@Override
	public String toXML()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("<").append("url");
		if (getName() != null && !getName().isEmpty())
		{
			buf.append(" name=\"").append(getName()).append("\"");
		}
		
		if (getUrl() != null && !getUrl().isEmpty())
		{
			buf.append(" url=\"").append(getUrl()).append("\"").append("/>");
		}
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		BookmarkedURL extension = (BookmarkedURL) super.clone();
		extension.name = this.name;
		extension.url = this.url;
		return extension;
	}

}
