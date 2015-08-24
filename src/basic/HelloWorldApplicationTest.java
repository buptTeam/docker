package basic;

import java.util.HashSet;
import java.util.Set;
 
import javax.ws.rs.core.Application;


import bean.ContainerBean;
import bean.ImageBean;
import bean.UserBean;
 
public class HelloWorldApplicationTest extends Application {
 
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(UserBean.class);
        classes.add(ImageBean.class);
        classes.add(ContainerBean.class);
       // classes.add(SubClass.class);
        return classes;
    }

//	@Override
//	public Set<Object> getSingletons() {
//		// TODO Auto-generated method stub
//		 Set<Object> s = new HashSet<Object>();
//		    
//		    // Register the Jackson provider for JSON
//		    
//		    // Make (de)serializer use a subset of JAXB and (afterwards) Jackson annotations
//		    // See http://wiki.fasterxml.com/JacksonJAXBAnnotations for more information
//		    ObjectMapper mapper = new ObjectMapper();
//		    AnnotationIntrospector primary = new JaxbAnnotationIntrospector();
//		    AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
//		    AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
//		    mapper.getDeserializationConfig().setAnnotationIntrospector(pair);
//		    mapper.getSerializationConfig().setAnnotationIntrospector(pair);
//		    
//		    // Set up the provider
//		    JacksonJaxbJsonProvider jaxbProvider = new JacksonJaxbJsonProvider();
//		    jaxbProvider.setMapper(mapper);
//		    
//		    s.add(jaxbProvider);
//		    return s;
//	}
    
 
}
