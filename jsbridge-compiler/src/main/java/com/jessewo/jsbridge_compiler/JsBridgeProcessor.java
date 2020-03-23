package com.jessewo.jsbridge_compiler;

import com.google.auto.service.AutoService;
import com.jessewo.annotations.JsBridgeWidget;
import com.jessewo.jsbridge_compiler.utils.Logger;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by wangzhx on 2020/3/23.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.jessewo.annotations.JsBridgeWidget"})
public class JsBridgeProcessor extends AbstractProcessor {

    private static final String PACKAGE_OF_GENERATE_FILE = "com.jessewoo.jsbridge";
    private static final String WARNING_TIPS = "DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY JS_BRIDGE.";

    private Types typesUtil;
    private Elements elementUtils;
    private Filer mFiler;
    private Logger logger;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typesUtil = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        logger = new Logger(processingEnv.getMessager());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (CollectionUtils.isNotEmpty(annotations)) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(JsBridgeWidget.class);
            logger.info(">>> Found routes, size is " + elements.size() + " <<<");
            try {
                List<MethodSpec> methodList = new ArrayList<>(elements.size());
                for (Element element : elements) {
                    if (element.getKind() != ElementKind.CLASS) {
                        logger.error(String.format("Builder annotation can only be applied to class: %s", element));
                        continue;
                    }
                    String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
                    String simpleName = element.getSimpleName().toString();
                    ClassName widgetClassName = ClassName.get(packageName, simpleName);

                    JsBridgeWidget annotation = element.getAnnotation(JsBridgeWidget.class);
                    String methodName = StringUtils.isEmpty(annotation.methodName()) ? simpleName : annotation.methodName();

                    methodList.add(
                            MethodSpec.methodBuilder(methodName)
                                    .addAnnotation(ClassName.get("android.webkit", "JavascriptInterface"))
                                    .addModifiers(PUBLIC)
                                    .returns(void.class)
                                    .addParameter(String.class, "jString")
                                    .addStatement("new $T().exec(jString)", widgetClassName)
                                    .build()
                    );
                }
                // Write root meta into disk.
                String packageName = elementUtils.getPackageOf(elements.iterator().next()).getQualifiedName().toString();
                String classFileName = "JsInterface";
                TypeSpec typeSpec = TypeSpec.classBuilder(classFileName)
                        .addJavadoc(WARNING_TIPS)
//                                .addSuperinterface(ClassName.get(elementUtils.getTypeElement(ITROUTE_ROOT)))
                        .addModifiers(PUBLIC)
                        .addMethods(methodList).build();
                JavaFile.builder(PACKAGE_OF_GENERATE_FILE, typeSpec).build().writeTo(mFiler);

                logger.info(">>> Generated Class, name is " + classFileName + " <<<");
            } catch (Exception e) {
                logger.error(e);
            }
            return true;
        }
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
