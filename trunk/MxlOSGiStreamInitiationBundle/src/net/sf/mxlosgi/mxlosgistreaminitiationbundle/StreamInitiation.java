package net.sf.mxlosgi.mxlosgistreaminitiationbundle;

import net.sf.mxlosgi.mxlosgidataformsbundle.DataForm;
import net.sf.mxlosgi.mxlosgiutilsbundle.StringUtils;
import net.sf.mxlosgi.mxlosgixmppbundle.PacketExtension;

/**
 * 
 * @author noah
 *
 */
public class StreamInitiation implements PacketExtension
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4657711225927436851L;

	public static final String ID_NOT_AVAILABLE = "ID_NOT_AVAILABLE";
	
	/**
	 * A prefix helps to make sure that ID's are unique across mutliple
	 * instances.
	 */
	private static String prefix = StringUtils.randomString(21) + "-";
	
	/**
	 * Keeps track of the current increment, which is appended to the
	 * prefix to forum a unique ID.
	 */
	private static long staticID = 0;
	
	/**
	 * Returns the next unique id. Each id made up of a short alphanumeric
	 * prefix along with a unique numeric value.
	 * 
	 * @return the next id.
	 */
	public static synchronized String nextID()
	{
		return prefix + Long.toString(staticID++);
	}

	private String id;

	private String mimeType;

	private XMPPFile file;

	private Feature feature;

	public StreamInitiation()
	{
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		if (ID_NOT_AVAILABLE.equals(id))
		{
			return null;
		}
		
		if (id == null || id.isEmpty())
		{
			id = nextID();
		}
		return id;
	}

	/**
	 * @param id
	 *                  the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * The "mime-type" attribute identifies the MIME-type for the data
	 * across the stream. This attribute MUST be a valid MIME-type as
	 * registered with the Internet Assigned Numbers Authority (IANA) [3]
	 * (specifically, as listed at
	 * <http://www.iana.org/assignments/media-types>). During negotiation,
	 * this attribute SHOULD be present, and is otherwise not required. If
	 * not included during negotiation, its value is assumed to be
	 * "binary/octect-stream".
	 * 
	 * @param mimeType
	 *                  The valid mime-type.
	 */
	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	/**
	 * Identifies the type of file that is desired to be transfered.
	 * 
	 * @return The mime-type.
	 * @see #setMimeType(String)
	 */
	public String getMimeType()
	{
		return mimeType;
	}

	/**
	 * Sets the file which contains the information pertaining to the file
	 * to be transfered.
	 * 
	 * @param file
	 *                  The file identified by the stream initiator to be
	 *                  sent.
	 */
	public void setFile(final XMPPFile file)
	{
		this.file = file;
	}

	/**
	 * Returns the file containing the information about the request.
	 * 
	 * @return Returns the file containing the information about the
	 *         request.
	 */
	public XMPPFile getFile()
	{
		return file;
	}

	/**
	 * Sets the data form which contains the valid methods of stream
	 * neotiation and transfer.
	 * 
	 * @param form
	 *                  The dataform containing the methods.
	 */
	public void setFeatureForm(DataForm form)
	{
		this.feature = new Feature(form);
	}

	/**
	 * Returns the data form which contains the valid methods of stream
	 * neotiation and transfer.
	 * 
	 * @return Returns the data form which contains the valid methods of
	 *         stream neotiation and transfer.
	 */
	public DataForm getFeatureForm()
	{
		return feature.getData();
	}

	@Override
	public String getElementName()
	{
		return "si";
	}

	@Override
	public String getNamespace()
	{
		return "http://jabber.org/protocol/si";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 */
	public String toXML()
	{
		StringBuilder buf = new StringBuilder();
		buf.append("<si xmlns=\"http://jabber.org/protocol/si\"");
		if (getId() != null)
		{
			buf.append(" id=\"").append(getId()).append("\"");
		}
		if (getMimeType() != null)
		{
			buf.append(" mime-type=\"").append(getMimeType()).append("\"");
		}
		buf.append(" profile=\"http://jabber.org/protocol/si/profile/file-transfer\">");

		// Add the file section if there is one.
		if (file != null)
		{
			String fileXML = file.toXML();
			if (fileXML != null)
			{
				buf.append(fileXML);
			}
		}
		if (feature != null)
		{
			buf.append(feature.toXML());
		}
		buf.append("</si>");
		return buf.toString();
	}


	@Override
	public Object clone() throws CloneNotSupportedException
	{
		StreamInitiation initiation = (StreamInitiation) super.clone();
		initiation.id = this.id;
		initiation.mimeType = this.mimeType;
		if (this.file != null)
		{
			initiation.file = (XMPPFile) this.file.clone();
		}
		if (this.feature != null)
		{
			initiation.feature = (Feature) this.feature.clone();
		}
		return initiation;
	}

	/**
	 * <ul>
	 * <li>size: The size, in bytes, of the data to be sent.</li>
	 * <li>name: The name of the file that the Sender wishes to send.</li>
	 * <li>date: The last modification time of the file. This is
	 * specified using the DateTime profile as described in Jabber Date
	 * and Time Profiles.</li>
	 * <li>hash: The MD5 sum of the file contents.</li>
	 * </ul>
	 * <p/> <p/> &lt;desc&gt; is used to provide a sender-generated
	 * description of the file so the receiver can better understand what
	 * is being sent. It MUST NOT be sent in the result. <p/> <p/> When
	 * &lt;range&gt; is sent in the offer, it should have no attributes.
	 * This signifies that the sender can do ranged transfers. When a
	 * Stream Initiation result is sent with the <range> element, it uses
	 * these attributes: <p/>
	 * <ul>
	 * <li>offset: Specifies the position, in bytes, to start
	 * transferring the file data from. This defaults to zero (0) if not
	 * specified.</li>
	 * <li>length - Specifies the number of bytes to retrieve starting at
	 * offset. This defaults to the length of the file from offset to the
	 * end.</li>
	 * </ul>
	 * <p/> <p/> Both attributes are OPTIONAL on the &lt;range&gt;
	 * element. Sending no attributes is synonymous with not sending the
	 * &lt;range&gt; element. When no &lt;range&gt; element is sent in the
	 * Stream Initiation result, the Sender MUST send the complete file
	 * starting at offset 0. More generally, data is sent over the stream
	 * byte for byte starting at the offset position for the length
	 * specified.
	 * 
	 * @author noah
	 */
	public static class XMPPFile implements PacketExtension
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5163128098428507057L;

		private String name;

		private long size;

		private String hash;

		private String date;

		private String desc;

		private boolean isRanged;
		
		private Long offset;
		
		private Long length;
		
		/**
		 * 
		 */
		public XMPPFile()
		{
		}

		/**
		 * Constructor providing the name of the file and its size.
		 * 
		 * @param name
		 *                  The name of the file.
		 * @param size
		 *                  The size of the file in bytes.
		 */
		public XMPPFile(String name)
		{
			this.name = name;
		}
		
		public XMPPFile(String name, long size)
		{
			this(name);
			this.size = size;
		}

		/**
		 * Returns the file's name.
		 * 
		 * @return Returns the file's name.
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * Returns the file's size.
		 * 
		 * @return Returns the file's size.
		 */
		public long getSize()
		{
			return size;
		}

		/**
		 * Sets the MD5 sum of the file's contents
		 * 
		 * @param hash
		 *                  The MD5 sum of the file's contents.
		 */
		public void setHash(final String hash)
		{
			this.hash = hash;
		}

		/**
		 * Returns the MD5 sum of the file's contents
		 * 
		 * @return Returns the MD5 sum of the file's contents
		 */
		public String getHash()
		{
			return hash;
		}

		/**
		 * Sets the date that the file was last modified.
		 * 
		 * @param date
		 *                  The date that the file was last
		 *                  modified.
		 */
		public void setDate(String date)
		{
			this.date = date;
		}

		/**
		 * Returns the date that the file was last modified.
		 * 
		 * @return Returns the date that the file was last modified.
		 */
		public String getDate()
		{
			return date;
		}

		/**
		 * Sets the description of the file.
		 * 
		 * @param desc
		 *                  The description of the file so that the
		 *                  file reciever can know what file it is.
		 */
		public void setDesc(final String desc)
		{
			this.desc = desc;
		}

		/**
		 * Returns the description of the file.
		 * 
		 * @return Returns the description of the file.
		 */
		public String getDesc()
		{
			return desc;
		}

		/**
		 * True if a range can be provided and false if it cannot.
		 * 
		 * @param isRanged
		 *                  True if a range can be provided and
		 *                  false if it cannot.
		 */
		public void setRanged(boolean isRanged)
		{
			this.isRanged = isRanged;
		}

		/**
		 * Returns whether or not the initiator can support a range
		 * for the file tranfer.
		 * 
		 * @return Returns whether or not the initiator can support
		 *         a range for the file tranfer.
		 */
		public boolean isRanged()
		{
			return isRanged;
		}

		/**
		 * @return the offset
		 */
		public Long getOffset()
		{
			return offset;
		}

		/**
		 * @param offset the offset to set
		 */
		public void setOffset(Long offset)
		{
			if (!isRanged())
			{
				throw new IllegalStateException("ranged is false");
			}
			this.offset = offset;
		}

		/**
		 * @return the length
		 */
		public Long getLength()
		{
			return length;
		}

		/**
		 * @param length the length to set
		 */
		public void setLength(Long length)
		{
			if (!isRanged())
			{
				throw new IllegalStateException("ranged is false");
			}
			this.length = length;
		}

		@Override
		public String getElementName()
		{
			return "file";
		}

		@Override
		public String getNamespace()
		{
			return "http://jabber.org/protocol/si/profile/file-transfer";
		}

		public String toXML()
		{
			StringBuilder buffer = new StringBuilder();

			buffer.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" ");

			if (getName() != null)
			{
				buffer.append("name=\"").append(StringUtils.escapeForXML(getName())).append("\" ");
			}

			if (getSize() > 0)
			{
				buffer.append("size=\"").append(getSize()).append("\" ");
			}

			if (getDate() != null)
			{
				buffer.append("date=\"").append(getDate()).append("\" ");
			}

			if (getHash() != null)
			{
				buffer.append("hash=\"").append(getHash()).append("\" ");
			}

			buffer.append(">");

			if (getDesc() != null && getDesc().length() > 0)
			{
				buffer.append("<desc>").append(StringUtils.escapeForXML(getDesc())).append("</desc>");
			}

			if (isRanged())
			{
				buffer.append("<range");
				if (getOffset() != null)
				{
					buffer.append(" offset=\"").append(getOffset()).append("\"");
				}
				if (getLength() != null)
				{
					buffer.append(" length=\"").append(getLength()).append("\"");
				}
				buffer.append("/>");
			}
			buffer.append("</").append(getElementName()).append(">");

			return buffer.toString();
		}

		@Override
		public Object clone() throws CloneNotSupportedException
		{
			XMPPFile file = (XMPPFile) super.clone();
			file.name = this.name;
			file.size = this.size;
			file.hash = this.hash;
			file.date = this.date;
			file.desc = this.desc;
			file.isRanged = this.isRanged;
			file.offset = this.offset;
			file.length = this.length;
			return file;
		}
		
		
	}

	/**
	 * The feature negotiation portion of the StreamInitiation packet.
	 * 
	 * @author Alexander Wenckus
	 * 
	 */
	public class Feature implements PacketExtension
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8467213960522999978L;
		
		private DataForm data;

		/**
		 * The dataform can be provided as part of the constructor.
		 * 
		 * @param data
		 *                  The dataform.
		 */
		public Feature(DataForm data)
		{
			this.data = data;
		}

		/**
		 * Returns the dataform associated with the feature
		 * negotiation.
		 * 
		 * @return Returns the dataform associated with the feature
		 *         negotiation.
		 */
		public DataForm getData()
		{
			return data;
		}

		public String getNamespace()
		{
			return "http://jabber.org/protocol/feature-neg";
		}

		public String getElementName()
		{
			return "feature";
		}

		public String toXML()
		{
			StringBuilder buf = new StringBuilder();
			buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
			buf.append(data.toXML());
			buf.append("</").append(getElementName()).append(">");
			return buf.toString();
		}

		@Override
		public Object clone() throws CloneNotSupportedException
		{
			Feature feature = (Feature) super.clone();
			if (this.data != null)
			{
				feature.data = (DataForm) this.data.clone();
			}
			
			return feature;
		}
		
		
	}

}