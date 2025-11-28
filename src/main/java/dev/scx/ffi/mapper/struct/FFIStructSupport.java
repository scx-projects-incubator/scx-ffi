package dev.scx.ffi.mapper.struct;

import dev.scx.ffi.type.FFIStruct;
import dev.scx.reflect.*;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.ArrayList;

import static java.lang.foreign.ValueLayout.*;

/// 内部构建辅助类
///
/// @author scx567888
/// @version 0.0.1
final class FFIStructSupport {

    public static Node createNode(Class<?> type) {
        return createNode(ScxReflect.typeOf(type), null, new NodeCreateContext());
    }

    public static Node createNode(TypeInfo typeInfo, FieldInfo fieldInfo, NodeCreateContext context) {

        // 1, 基本类型
        if (typeInfo instanceof PrimitiveTypeInfo primitiveTypeInfo) {
            var valueLayout = getPrimitiveValueLayout(primitiveTypeInfo.rawClass());
            return new PrimitiveNode(fieldInfo, valueLayout);
        }

        // 2, MemorySegment 类型
        if (typeInfo.rawClass() == MemorySegment.class) {
            return new MemorySegmentNode(fieldInfo);
        }

        // 3, 结构体类型
        if (FFIStruct.class.isAssignableFrom(typeInfo.rawClass())) {

            // 只处理 classInfo
            if (typeInfo instanceof ClassInfo classInfo) {
                // 检查递归嵌套
                context.startRecursionCheck(classInfo);

                try {
                    // 获取 属于 结构体的字段
                    var structFields = getStructFields(classInfo.allFields());
                    var fieldNodes = new Node[structFields.length];

                    for (int i = 0; i < structFields.length; i = i + 1) {
                        var structField = structFields[i];
                        fieldNodes[i] = createNode(structField.fieldType(), structField, context);
                    }

                    return new StructNode(fieldInfo, fieldNodes, classInfo);
                } finally {
                    context.endRecursionCheck();
                }
            }

            // 必须是 ClassInfo todo 这里要不要限制必须是一个可以实例化的类型?
            throw new IllegalArgumentException("ffiStruct type not ClassInfo, type: " + typeInfo);
        }

        // 其余全不支持 !!!
        throw new IllegalArgumentException("不支持的 结构体字段 类型 !!! " + typeInfo);
    }

    /// 需要 过滤 + 排序
    static FieldInfo[] getStructFields(FieldInfo[] fieldInfos) {
        // todo 这里需要 注解排序
        var result = new ArrayList<FieldInfo>();
        for (var f : fieldInfos) {
            // 只处理非静态的 public 字段
            if (f.accessModifier() == AccessModifier.PUBLIC && !f.isStatic()) {
                result.add(f);
            }
        }
        return result.toArray(FieldInfo[]::new);
    }

    public static ValueLayout getPrimitiveValueLayout(Class<?> type) {
        if (type == byte.class) {
            return JAVA_BYTE;
        }
        if (type == short.class) {
            return JAVA_SHORT;
        }
        if (type == int.class) {
            return JAVA_INT;
        }
        if (type == long.class) {
            return JAVA_LONG;
        }
        if (type == float.class) {
            return JAVA_FLOAT;
        }
        if (type == double.class) {
            return JAVA_DOUBLE;
        }
        if (type == boolean.class) {
            return JAVA_BOOLEAN;
        }
        if (type == char.class) {
            return JAVA_CHAR;
        }
        // 这里几乎不可能发生
        throw new IllegalArgumentException("type not primitive: " + type);
    }

}
