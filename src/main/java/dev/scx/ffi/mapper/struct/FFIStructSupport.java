package dev.scx.ffi.mapper.struct;

import dev.scx.ffi.type.FFIStruct;
import dev.scx.reflect.*;

import java.lang.foreign.MemorySegment;

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
            return new PrimitiveNode(fieldInfo, primitiveTypeInfo);
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

                    var structNode = new StructNode(fieldInfo, classInfo);

                    // todo 这里还要根据 注解排序
                    for (var f : classInfo.allFields()) {
                        // 只处理非静态的 public 字段
                        if (f.accessModifier() == AccessModifier.PUBLIC && !f.isStatic()) {
                            var fieldNode = createNode(f.fieldType(), f, context);
                            structNode.addFieldNode(fieldNode);
                        }
                    }

                    return structNode;
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

}
