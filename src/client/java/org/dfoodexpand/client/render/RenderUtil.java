package org.dfoodexpand.client.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.util.function.Consumer;

public class RenderUtil {
    public static final Consumer<MatrixStack> BASE_MUL = matrixStack -> {
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
    };
}
