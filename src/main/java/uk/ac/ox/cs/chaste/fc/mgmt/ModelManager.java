/**
 * 
 */
package uk.ac.ox.cs.chaste.fc.mgmt;

import java.sql.ResultSet;
import java.sql.SQLException;

import uk.ac.ox.cs.chaste.fc.beans.ChasteEntity;
import uk.ac.ox.cs.chaste.fc.beans.Notifications;
import uk.ac.ox.cs.chaste.fc.beans.User;


/**
 * @author martin
 *
 * TODO: optimize (hashtables etc)
 * 
 * TODO: is current user allowed to see the stuff?
 */
public class ModelManager
extends ChasteEntityManager
{
	
	private static final String SQL_SELECT_BEGIN =  
		"SELECT u.id AS versionauthor,"
		+ " m.id AS versionid,"
		+ " m.version AS versionname,"
		+ " m.created AS versioncreated,"
		+ " m.filepath AS versionfilepath,"
		+ " m.visibility AS visibility,"
		+ " m.commitmsg AS commitmsg,"
		+ " u2.id AS entityauthor,"
		+ " mo.id AS entityid,"
		+ " mo.name AS entityname,"
		+ " mo.created AS entitycreated,"
		+ " COUNT(mf.file) AS numfiles"
		+ " FROM       `modelversions` m"
		+ " INNER JOIN `user` u on m.author = u.id"
		+ " INNER JOIN `model_files` mf on mf.model = m.id"
		+ " INNER JOIN `models` mo on m.model=mo.id"
		+ " INNER JOIN `user` u2 on mo.author = u2.id";
	private static final String SQL_SELECT_END = 
		" GROUP BY m.id"
		+ " ORDER BY mo.name, m.version";
	
	public ModelManager (DatabaseConnector db, Notifications note, UserManager userMgmt, User user)
	{
		super (db, note, userMgmt, user);
		this.entityTable = "models";
		this.entityVersionsTable= "modelversions";
		this.entityColumn = "model";
		this.entityFilesTable = "model_files";
		this.entityStorageDir = Tools.getModelStorageDir ();
	}

	@Override
	protected String buildSelectQuery (String where)
	{
		return SQL_SELECT_BEGIN + where + SQL_SELECT_END;
	}

	@Override
	protected ChasteEntity createEntity (ResultSet rs) throws SQLException
	{
		return new ChasteEntity (
			rs.getInt ("entityid"),
			rs.getString ("entityname"),
			userMgmt.getUser (rs.getInt ("entityauthor")),
			rs.getTimestamp ("entitycreated"),
			"model"
			);
	}
}
