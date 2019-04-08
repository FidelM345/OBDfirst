package com.fr3ts0n.ecu.gui.androbd;

public class CarFailuresModel
{
    private String partname;

    private String errordesc;

    private String id;

    private String errorcode;


    public CarFailuresModel(String id, String errorcode, String errordesc, String partname) {
        this.partname = partname;
        this.errordesc = errordesc;
        this.id = id;
        this.errorcode = errorcode;
    }


    public String getPartname ()
    {
        return partname;
    }

    public void setPartname (String partname)
    {
        this.partname = partname;
    }

    public String getErrordesc ()
    {
        return errordesc;
    }

    public void setErrordesc (String errordesc)
    {
        this.errordesc = errordesc;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getErrorcode ()
    {
        return errorcode;
    }

    public void setErrorcode (String errorcode)
    {
        this.errorcode = errorcode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [partname = "+partname+", errordesc = "+errordesc+", id = "+id+", errorcode = "+errorcode+"]";
    }
}