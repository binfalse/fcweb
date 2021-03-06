/**
 * 
 */
package uk.ac.ox.cs.chaste.fc.beans;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import uk.ac.ox.cs.chaste.fc.mgmt.Tools;


/**
 * @author martin
 *
 */
public class ChasteEntityVersion
{
	// Note that these must match the `visibility` fields in the `*versions` tables
	public static final String VISIBILITY_PRIVATE = "PRIVATE";
	public static final String VISIBILITY_RESTRICTED = "RESTRICTED";
	public static final String VISIBILITY_PUBLIC = "PUBLIC";
	public static final String VISIBILITY_MODERATED = "MODERATED";
	
	private ChasteEntity entity;
	private int id;
	private String version;
	private User author;
	private String filePath;
	private String visibility;
	private String url;
	private Timestamp created;
	private int numFiles;
	private Vector<ChasteFile> files;
	private Vector<ChasteExperiment> experiments;
	private String commitMsg;
	// These represent the protocol interface (at present model interface info is not stored)
	private boolean hasInterface;
	private int numOntologyTerms;
	private HashMap<String, Boolean> ontologyTerms;
	
	public ChasteEntityVersion (ChasteEntity entity, int id, String version, User author, String filePath, Timestamp created, int numFiles, String visibility, String commitMsg, int numTerms)
	{
		this.entity = entity;
		this.id = id;
		this.version = version;
		this.url = Tools.convertForURL (version);
		this.created = created;
		this.author = author;
		this.filePath = filePath;
		this.numFiles = numFiles;
		this.visibility = visibility;
		files = new Vector<ChasteFile> ();
		experiments = new Vector<ChasteExperiment> ();
		this.commitMsg = commitMsg;
		this.numOntologyTerms = numTerms;
		this.hasInterface = (numTerms > 1);
	}
	
	public ChasteFile getFileById (int id)
	{
		for (ChasteFile cf : files)
			if (cf.getId () == id)
				return cf;
		return null;
	}
	
	
	public String getUrl ()
	{
		return url;
	}
	
	
	public String getCommitMessage ()
	{
		return commitMsg;
	}
	
	
	public String getFilePath ()
	{
		return filePath;
	}

	public int getId ()
	{
		return id;
	}

	
	public void setFiles (Vector<ChasteFile> files)
	{
		this.files = files;
	}

	
	public Vector<ChasteFile> getFiles ()
	{
		return files;
	}
	
	
	
	public void addFile (ChasteFile file)
	{
		this.files.add (file);
	}
	
	
	
	public void addExperiment (ChasteExperiment ev)
	{
		this.experiments.add (ev);
	}
	
	
	public int getNumFiles ()
	{
		return numFiles;
	}



	public User getAuthor ()
	{
		return author;
	}

	public String getVisibility ()
	{
		return visibility;
	}
	
	/**
	 * Get the index of a visibility string for easy comparison of 'privateness'.
	 * @param visibility a ChasteEntityVersion.VISIBILITY_* string
	 * @return an index in [1,4] where 1 is PRIVATE, 4 is MODERATED; 0 if bad input
	 */
	public static int getVisibilityIndex(String visibility)
	{
		int result;
		if (visibility.equals(ChasteEntityVersion.VISIBILITY_PRIVATE))
			result = 1;
		else if (visibility.equals(ChasteEntityVersion.VISIBILITY_RESTRICTED))
			result = 2;
		else if (visibility.equals(ChasteEntityVersion.VISIBILITY_PUBLIC))
			result = 3;
		else if (visibility.equals(ChasteEntityVersion.VISIBILITY_MODERATED))
			result = 4;
		else
			result = 0;
		return result;
	}
	
	public static boolean isValidVisibility(String visibility)
	{
		return (getVisibilityIndex(visibility) != 0);
	}
	
	public String getJointVisibility(ChasteEntityVersion other)
	{
		String result = this.visibility;
		if (getVisibilityIndex(this.visibility) > getVisibilityIndex(other.visibility))
		{
			// other is less public than we are
			result = other.visibility;
		}
		return result;
	}
	
	public String getCreated ()
	{
		return Tools.formatTimeStamp (created);
	}


	public ChasteEntity getEntity ()
	{
		return entity;
	}

	
	public String getVersion ()
	{
		return version;
	}

	public String getName ()
	{
		return entity.getName ();
	}
	
	public boolean parsedOk()
	{
		return numOntologyTerms > 0;
	}

	public boolean hasInterface()
	{
		return hasInterface;
	}
	
	public HashMap<String, Boolean> getOntologyTerms() throws IOException
	{
		if (this.ontologyTerms.isEmpty())
			throw new IOException("Ontology terms for entity version have not yet been loaded.");
		return ontologyTerms;
	}

	public void setOntologyTerms(HashMap<String, Boolean> ontologyTerms)
	{
		assert(ontologyTerms.size() == this.numOntologyTerms);
		this.ontologyTerms = ontologyTerms;
//		this.ontologyTerms.remove(""); // Remove the flag that says "we analysed this protocol's interface"
	}

	public void debug ()
	{
		System.out.println ("\t" + getVersion ());
	}


	@SuppressWarnings("unchecked")
	public JSONObject toJson ()
	{
		JSONObject json = new JSONObject ();

		json.put ("version", version);
		json.put ("created", getCreated ());
		json.put ("author", getAuthor ().getNick ());
		json.put ("numFiles", numFiles);
		json.put ("visibility", visibility);
		json.put ("id", id);
		json.put ("entityId", entity.getId ());
		json.put ("name", entity.getName ());
		json.put ("commitMessage", getCommitMessage ());
		json.put ("parsedOk", parsedOk());

		if (files != null && files.size () > 0)
		{
			JSONArray f = new JSONArray ();
			for (ChasteFile cf : files)
				f.add (cf.toJson ());
			json.put ("files", f);
		}
		
		if (experiments != null && experiments.size () > 0)
		{
			JSONArray e = new JSONArray ();
			for (ChasteExperiment ev : experiments)
				e.add (ev.toJson ());
			json.put ("experiments", e);
		}
		
		return json;
	}
	
	
}
