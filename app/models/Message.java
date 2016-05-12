package models;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Message extends Model {

	@Id
	public Long id;
	@Required
	public String name;
	@Email
	public String mail;
	public String message;
	public Date postdate;

	public static Finder<Long, Message> find = new Finder<Long, Message>(Long.class, Message.class);

	@Override
	public String toString() {
		return ("[id:" + id + ", name:" + name + ",mail:" + mail + ",message:" + message + ",date:" + postdate + "]");
	}
}