package com.cmeza.sdgenerator.plugin;

import com.cmeza.sdgenerator.util.Constants;
import com.cmeza.sdgenerator.util.SDLogger;
import com.cmeza.sdgenerator.util.SDMojoException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Created by carlos on 01/05/17.
 */
public abstract class CommonsMojo extends AbstractMojo {

    @Parameter(name = Constants.ENTITY_PACKAGE)
    protected String[] entityPackage;

    @Parameter(name = Constants.REPOSITORY_PACKAGE)
    protected String repositoryPackage;

    @Parameter(name = Constants.REPOSITORY_POSTFIX, defaultValue = "Repository")
    protected String repositoryPostfix;
    
    @Parameter(name = Constants.REPOSITORY_FLAG, defaultValue = "true")
    protected Boolean repositoryFlag;
    
    @Parameter(name = Constants.REPOSITORY_FINDERBY, defaultValue = "false")
    protected Boolean repositoryFinderBy;

    @Parameter(name = Constants.MAGANER_PACKAGE)
    protected String managerPackage;

    @Parameter(name = Constants.MANAGER_POSTFIX, defaultValue = "Manager")
    protected String managerPostfix;

    @Parameter(name = Constants.ONLY_ANNOTATIONS, defaultValue = "false")
    protected Boolean onlyAnnotations;

    @Parameter(name = Constants.OVERWRITE, defaultValue = "false")
    protected Boolean overwrite;

    @Component
    protected MavenProject project;

    public void validateField(String parameter) throws SDMojoException {

        boolean errorFound = Boolean.FALSE;

        switch (parameter) {
            case Constants.ENTITY_PACKAGE:
                if (entityPackage == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            case Constants.REPOSITORY_PACKAGE:
                if (repositoryPackage == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            case Constants.REPOSITORY_POSTFIX:
                if (repositoryPostfix == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            case Constants.MAGANER_PACKAGE:
                if (managerPackage == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            case Constants.MANAGER_POSTFIX:
                if (managerPostfix == null) {
                    errorFound = Boolean.TRUE;
                }
                break;
            default:
                SDLogger.addError( String.format("%s configuration parameter not found!", parameter));
                throw new SDMojoException();
        }

        if (errorFound) {
            SDLogger.addError( String.format("%s configuration not found!", parameter));
            throw new SDMojoException();
        }
    }
}
