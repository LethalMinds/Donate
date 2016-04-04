package com.lethalminds.udonate.server.model.callbacks;

/**
 * Created by Nishok on 4/2/2016.
 */
public interface GetCallback {
    public abstract <T extends Object> void done(T items);
}
