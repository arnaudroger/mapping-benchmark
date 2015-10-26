package org.simpleflatmapper.db;

import javax.naming.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by aroger on 17/10/2015.
 */
public class InitialContextFactory implements javax.naming.spi.InitialContextFactory {

    private static Map<String, Object> map = new ConcurrentHashMap<>();

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return new Context() {
            @Override
            public Object lookup(Name name) throws NamingException {
                return map.get(name.toString());
            }

            @Override
            public Object lookup(String name) throws NamingException {
                return map.get(name);
            }

            @Override
            public void bind(Name name, Object obj) throws NamingException {
                map.put(name.toString(), obj);
            }

            @Override
            public void bind(String name, Object obj) throws NamingException {
                map.put(name, obj);
            }

            @Override
            public void rebind(Name name, Object obj) throws NamingException {
                bind(name, obj);
            }

            @Override
            public void rebind(String name, Object obj) throws NamingException {
                bind(name, obj);
            }

            @Override
            public void unbind(Name name) throws NamingException {
                map.remove(name.toString());
            }

            @Override
            public void unbind(String name) throws NamingException {
                map.remove(name);
            }

            @Override
            public void rename(Name oldName, Name newName) throws NamingException {
                map.put(newName.toString(), map.remove(oldName.toString()));
            }

            @Override
            public void rename(String oldName, String newName) throws NamingException {
                map.put(newName, map.remove(oldName));
            }

            @Override
            public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void destroySubcontext(Name name) throws NamingException {
            }

            @Override
            public void destroySubcontext(String name) throws NamingException {
            }

            @Override
            public Context createSubcontext(Name name) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public Context createSubcontext(String name) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object lookupLink(Name name) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object lookupLink(String name) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public NameParser getNameParser(Name name) throws NamingException {
                return new NameParser() {
                    @Override
                    public Name parse(String name) throws NamingException {
                        return new CompositeName(name);
                    }
                };
            }

            @Override
            public NameParser getNameParser(String name) throws NamingException {
                return new NameParser() {
                    @Override
                    public Name parse(String name) throws NamingException {
                        return new CompositeName(name);
                    }
                };
            }

            @Override
            public Name composeName(Name name, Name prefix) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public String composeName(String name, String prefix) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object addToEnvironment(String propName, Object propVal) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object removeFromEnvironment(String propName) throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public Hashtable<?, ?> getEnvironment() throws NamingException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void close() throws NamingException {

            }

            @Override
            public String getNameInNamespace() throws NamingException {
                throw new UnsupportedOperationException();
            }
        };
    }
}
