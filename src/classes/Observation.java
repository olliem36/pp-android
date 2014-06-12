package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Observation implements Serializable
{
	public int onlineObservationID;
	public int mobileObservationID;
	public String title;
	public Date date;
	public ArrayList<User> children = new ArrayList<User>();
}
