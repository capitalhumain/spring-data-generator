package com.cmeza.sdgenerator.support.maker;

import com.cmeza.sdgenerator.support.maker.builder.ObjectBuilder;
import com.cmeza.sdgenerator.support.maker.builder.ObjectStructure;
import com.cmeza.sdgenerator.support.maker.values.ExpressionValues;
import com.cmeza.sdgenerator.support.maker.values.ObjectTypeValues;
import com.cmeza.sdgenerator.support.maker.values.ObjectValues;
import com.cmeza.sdgenerator.support.maker.values.ScopeValues;
import com.cmeza.sdgenerator.util.CustomResourceLoader;
import com.cmeza.sdgenerator.util.GeneratorUtils;
import com.cmeza.sdgenerator.util.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by carlos on 08/04/17.
 */
public class ManagerStructure {

    private ObjectBuilder objectBuilder;

    public ManagerStructure(String managerPackage, String entityName, String postfix, String repositoryPackage, String repositoryPostfix, CustomResourceLoader loader) {

        String managerName = entityName + postfix;
        String repositoryName = entityName + repositoryPostfix;
        String repositoryNameAttribute = GeneratorUtils.decapitalize(repositoryName);
        
        Boolean useSpecificAnnotation = loader.isUseSpecificAnnotation();
        
        Class<?> clazzAnnotationSpring = Component.class;
        if(useSpecificAnnotation != null && useSpecificAnnotation) clazzAnnotationSpring = Service.class;

        this.objectBuilder = new ObjectBuilder(new ObjectStructure(managerPackage, ScopeValues.PUBLIC, ObjectTypeValues.CLASS, managerName)
                .addImport(repositoryPackage + "." + repositoryName)
                .addImport(Autowired.class)
                .addImport(clazzAnnotationSpring)
                .addAnnotation(clazzAnnotationSpring)
                .addAttribute(repositoryName, repositoryNameAttribute)
                .addConstructor(new ObjectStructure.ObjectConstructor(ScopeValues.PUBLIC, managerName)
                        .addAnnotation(Autowired.class)
                        .addArgument(repositoryName, repositoryNameAttribute)
                        .addBodyLine(ObjectValues.THIS.getValue() + repositoryNameAttribute + ExpressionValues.EQUAL.getValue() + repositoryNameAttribute)
                )
        ).setAttributeBottom(true);

    }

    public Tuple<String, Integer> build(){
        return new Tuple<>(objectBuilder == null ? null : objectBuilder.build(), 0);
    }
}
