/*
 * MIT License
 *
 * Copyright (c) 2018 Victor Schappert
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.tarro.base.bytecode;

/**
 * <p>
 * Container for all opcode values in the Java Virtual Machine instruction set,
 * across all versions of the JVM.
 * </p>
 *
 * <p>
 * This class is simply a container for integer constants representing each and
 * every known opcode byte. This simplicity maybe useful for some applications,
 * while others will benefit from using the {@link Opcode} interface and the
 * enumerations which implement it.
 * </p>
 *
 * @author Victor Schappert
 * @since 20171130
 * @see Opcode
 */
public final class OpcodeValue {

    //
    // PUBLIC CONSTANTS
    //

    /**
     * {@code aastore} ({@value AALOAD})
     *
     * @see NoOperandOpcode#AALOAD
     */
    public static final int AALOAD = 0x32;
    /**
     * {@code aastore} ({@value AASTORE})
     *
     * @see NoOperandOpcode#AASTORE
     */
    public static final int AASTORE = 0x53;
    /**
     * {@code aconst_null} ({@value ACONST_NULL})
     *
     * @see NoOperandOpcode#ACONST_NULL
     */
    public static final int ACONST_NULL = 0x01;
    /**
     * {@code aload} ({@value ALOAD})
     *
     * @see OneOperandOpcode#ALOAD
     */
    public static final int ALOAD = 0x19;
    /**
     * {@code aload_0} ({@value ALOAD_0})
     *
     * @see NoOperandOpcode#ALOAD_0
     */
    public static final int ALOAD_0 = 0x2a;
    /**
     * {@code aload_1} ({@value ALOAD_1})
     *
     * @see NoOperandOpcode#ALOAD_1
     */
    public static final int ALOAD_1 = 0x2b;
    /**
     * {@code aload_2} ({@value ALOAD_2})
     *
     * @see NoOperandOpcode#ALOAD_2
     */
    public static final int ALOAD_2 = 0x2c;
    /**
     * {@code aload_3} ({@value ALOAD_3})
     *
     * @see NoOperandOpcode#ALOAD_3
     */
    public static final int ALOAD_3 = 0x2d;
    /**
     * {@code anewarray} ({@value ANEWARRAY})
     *
     * @see OneOperandOpcode#ANEWARRAY
     */
    public static final int ANEWARRAY = 0xbd;
    /**
     * {@code areturn} ({@value ARETURN})
     *
     * @see NoOperandOpcode#ARETURN
     */
    public static final int ARETURN = 0xb0;
    /**
     * {@code arraylength} ({@value ARRAYLENGTH})
     *
     * @see NoOperandOpcode#ARRAYLENGTH
     */
    public static final int ARRAYLENGTH = 0xbe;
    /**
     * {@code astore} ({@value ASTORE})
     *
     * @see OneOperandOpcode#ASTORE
     */
    public static final int ASTORE = 0x3a;
    /**
     * {@code astore_0} ({@value ASTORE_0})
     *
     * @see NoOperandOpcode#ASTORE_0
     */
    public static final int ASTORE_0 = 0x4b;
    /**
     * {@code astore_1} ({@value ASTORE_1})
     *
     * @see NoOperandOpcode#ASTORE_1
     */
    public static final int ASTORE_1 = 0x4c;
    /**
     * {@code astore_2} ({@value ASTORE_2})
     *
     * @see NoOperandOpcode#ASTORE_2
     */
    public static final int ASTORE_2 = 0x4d;
    /**
     * {@code astore_3} ({@value ASTORE_3})
     *
     * @see NoOperandOpcode#ASTORE_3
     */
    public static final int ASTORE_3 = 0x4e;
    /**
     * {@code athrow} ({@value ATHROW})
     *
     * @see NoOperandOpcode#ATHROW
     */
    public static final int ATHROW = 0xbf;
    /**
     * {@code baload} ({@value BALOAD})
     *
     * @see NoOperandOpcode#BALOAD
     */
    public static final int BALOAD = 0x33;
    /**
     * {@code bastore} ({@value BASTORE})
     *
     * @see NoOperandOpcode#BASTORE
     */
    public static final int BASTORE = 0x54;
    /**
     * {@code bipush} ({@value BIPUSH})
     *
     * @see OneOperandOpcode#BIPUSH
     */
    public static final int BIPUSH = 0x10;
    /**
     * {@code breakpoint} ({@value BREAKPOINT})
     *
     * @see NoOperandOpcode#BREAKPOINT
     */
    public static final int BREAKPOINT = 0xca;
    /**
     * {@code caload} ({@value CALOAD})
     *
     * @see NoOperandOpcode#CALOAD
     */
    public static final int CALOAD = 0x34;
    /**
     * {@code castore} ({@value CASTORE})
     *
     * @see NoOperandOpcode#CASTORE
     */
    public static final int CASTORE = 0x55;
    /**
     * {@code checkcast} ({@value CHECKCAST})
     *
     * @see OneOperandOpcode#CHECKCAST
     */
    public static final int CHECKCAST = 0xc0;
    /**
     * {@code d2f} ({@value D2F})
     *
     * @see NoOperandOpcode#D2F
     */
    public static final int D2F = 0x90;
    /**
     * {@code d2i} ({@value D2I})
     *
     * @see NoOperandOpcode#D2I
     */
    public static final int D2I = 0x8e;
    /**
     * {@code d2l} ({@value D2L})
     *
     * @see NoOperandOpcode#D2L
     */
    public static final int D2L = 0x8f;
    /**
     * {@code dadd} ({@value DADD})
     *
     * @see NoOperandOpcode#DADD
     */
    public static final int DADD = 0x63;
    /**
     * {@code daload} ({@value DALOAD})
     *
     * @see NoOperandOpcode#DALOAD
     */
    public static final int DALOAD = 0x31;
    /**
     * {@code dastore} ({@value DASTORE})
     *
     * @see NoOperandOpcode#DASTORE
     */
    public static final int DASTORE = 0x52;
    /**
     * {@code dcmpg} ({@value DCMPG})
     *
     * @see NoOperandOpcode#DCMPG
     */
    public static final int DCMPG = 0x98;
    /**
     * {@code dcmpl} ({@value DCMPL})
     *
     * @see NoOperandOpcode#DCMPL
     */
    public static final int DCMPL = 0x97;
    /**
     * {@code dconst_0} ({@value DCONST_0})
     *
     * @see NoOperandOpcode#DCONST_0
     */
    public static final int DCONST_0 = 0x0e;
    /**
     * {@code dconst_1} ({@value DCONST_1})
     *
     * @see NoOperandOpcode#DCONST_1
     */
    public static final int DCONST_1 = 0x0f;
    /**
     * {@code ddiv} ({@value DDIV})
     *
     * @see NoOperandOpcode#DDIV
     */
    public static final int DDIV = 0x6f;
    /**
     * {@code dload} ({@value DLOAD})
     *
     * @see OneOperandOpcode#DLOAD
     */
    public static final int DLOAD = 0x18;
    /**
     * {@code dload_0} ({@value DLOAD_0})
     *
     * @see NoOperandOpcode#DLOAD_0
     */
    public static final int DLOAD_0 = 0x26;
    /**
     * {@code dload_1} ({@value DLOAD_1})
     *
     * @see NoOperandOpcode#DLOAD_1
     */
    public static final int DLOAD_1 = 0x27;
    /**
     * {@code dload_2} ({@value DLOAD_2})
     *
     * @see NoOperandOpcode#DLOAD_2
     */
    public static final int DLOAD_2 = 0x28;
    /**
     * {@code dload_3} ({@value DLOAD_3})
     *
     * @see NoOperandOpcode#DLOAD_3
     */
    public static final int DLOAD_3 = 0x29;
    /**
     * {@code dmul} ({@value DMUL})
     *
     * @see NoOperandOpcode#DMUL
     */
    public static final int DMUL = 0x6b;
    /**
     * {@code dneg} ({@value DNEG})
     *
     * @see NoOperandOpcode#DNEG
     */
    public static final int DNEG = 0x77;
    /**
     * {@code drem} ({@value DREM})
     *
     * @see NoOperandOpcode#DREM
     */
    public static final int DREM = 0x73;
    /**
     * {@code dreturn} ({@value DRETURN})
     *
     * @see NoOperandOpcode#DRETURN
     */
    public static final int DRETURN = 0xaf;
    /**
     * {@code dstore} ({@value DSTORE})
     *
     * @see OneOperandOpcode#DSTORE
     */
    public static final int DSTORE = 0x39;
    /**
     * {@code dstore_0} ({@value DSTORE_0})
     *
     * @see NoOperandOpcode#DSTORE_0
     */
    public static final int DSTORE_0 = 0x47;
    /**
     * {@code dstore_1} ({@value DSTORE_1})
     *
     * @see NoOperandOpcode#DSTORE_1
     */
    public static final int DSTORE_1 = 0x48;
    /**
     * {@code dstore_2} ({@value DSTORE_2})
     *
     * @see NoOperandOpcode#DSTORE_2
     */
    public static final int DSTORE_2 = 0x49;
    /**
     * {@code dstore_3} ({@value DSTORE_3})
     *
     * @see NoOperandOpcode#DSTORE_3
     */
    public static final int DSTORE_3 = 0x4a;
    /**
     * {@code dsub} ({@value DSUB})
     *
     * @see NoOperandOpcode#DSUB
     */
    public static final int DSUB = 0x67;
    /**
     * {@code dup} ({@value DUP})
     *
     * @see NoOperandOpcode#DUP
     */
    public static final int DUP = 0x59;
    /**
     * {@code dup_x1} ({@value DUP_X1})
     *
     * @see NoOperandOpcode#DUP_X1
     */
    public static final int DUP_X1 = 0x5a;
    /**
     * {@code dup_x2} ({@value DUP_X2})
     *
     * @see NoOperandOpcode#DUP_X2
     */
    public static final int DUP_X2 = 0x5b;
    /**
     * {@code dup2} ({@value DUP2})
     *
     * @see NoOperandOpcode#DUP2
     */
    public static final int DUP2 = 0x5c;
    /**
     * {@code dup2_x1} ({@value DUP2_X1})
     *
     * @see NoOperandOpcode#DUP2_X1
     */
    public static final int DUP2_X1 = 0x5d;
    /**
     * {@code dup2_x2} ({@value DUP2_X2})
     *
     * @see NoOperandOpcode#DUP2_X2
     */
    public static final int DUP2_X2 = 0x5e;
    /**
     * {@code f2d} ({@value F2D})
     *
     * @see NoOperandOpcode#F2D
     */
    public static final int F2D = 0x8d;
    /**
     * {@code f2i} ({@value F2I})
     *
     * @see NoOperandOpcode#F2I
     */
    public static final int F2I = 0x8b;
    /**
     * {@code f2l} ({@value F2L})
     *
     * @see NoOperandOpcode#F2L
     */
    public static final int F2L = 0x8c;
    /**
     * {@code fadd} ({@value FADD})
     *
     * @see NoOperandOpcode#FADD
     */
    public static final int FADD = 0x62;
    /**
     * {@code faload} ({@value FALOAD})
     *
     * @see NoOperandOpcode#FALOAD
     */
    public static final int FALOAD = 0x30;
    /**
     * {@code fastore} ({@value FASTORE})
     *
     * @see NoOperandOpcode#FASTORE
     */
    public static final int FASTORE = 0x51;
    /**
     * {@code fcmpg} ({@value FCMPG})
     *
     * @see NoOperandOpcode#FCMPG
     */
    public static final int FCMPG = 0x96;
    /**
     * {@code fcmpl} ({@value FCMPL})
     *
     * @see NoOperandOpcode#FCMPL
     */
    public static final int FCMPL = 0x95;
    /**
     * {@code fconst_0} ({@value FCONST_0})
     *
     * @see NoOperandOpcode#FCONST_0
     */
    public static final int FCONST_0 = 0x0b;
    /**
     * {@code fconst_1} ({@value FCONST_1})
     *
     * @see NoOperandOpcode#FCONST_1
     */
    public static final int FCONST_1 = 0x0c;
    /**
     * {@code fconst_2} ({@value FCONST_2})
     *
     * @see NoOperandOpcode#FCONST_2
     */
    public static final int FCONST_2 = 0x0d;
    /**
     * {@code fdiv} ({@value FDIV})
     *
     * @see NoOperandOpcode#FDIV
     */
    public static final int FDIV = 0x6e;
    /**
     * {@code fload} ({@value FLOAD})
     *
     * @see OneOperandOpcode#FLOAD
     */
    public static final int FLOAD = 0x17;
    /**
     * {@code fload_0} ({@value FLOAD_0})
     *
     * @see NoOperandOpcode#FLOAD_0
     */
    public static final int FLOAD_0 = 0x22;
    /**
     * {@code fload_1} ({@value FLOAD_1})
     *
     * @see NoOperandOpcode#FLOAD_1
     */
    public static final int FLOAD_1 = 0x23;
    /**
     * {@code fload_2} ({@value FLOAD_2})
     *
     * @see NoOperandOpcode#FLOAD_2
     */
    public static final int FLOAD_2 = 0x24;
    /**
     * {@code fload_3} ({@value FLOAD_3})
     *
     * @see NoOperandOpcode#FLOAD_3
     */
    public static final int FLOAD_3 = 0x25;
    /**
     * {@code fmul} ({@value FMUL})
     *
     * @see NoOperandOpcode#FMUL
     */
    public static final int FMUL = 0x6a;
    /**
     * {@code fneg} ({@value FNEG})
     *
     * @see NoOperandOpcode#FNEG
     */
    public static final int FNEG = 0x76;
    /**
     * {@code frem} ({@value FREM})
     *
     * @see NoOperandOpcode#FREM
     */
    public static final int FREM = 0x72;
    /**
     * {@code freturn} ({@value FRETURN})
     *
     * @see NoOperandOpcode#FRETURN
     */
    public static final int FRETURN = 0xae;
    /**
     * {@code fstore} ({@value FSTORE})
     *
     * @see OneOperandOpcode#FSTORE
     */
    public static final int FSTORE = 0x38;
    /**
     * {@code fstore_0} ({@value FSTORE_0})
     *
     * @see NoOperandOpcode#FSTORE_0
     */
    public static final int FSTORE_0 = 0x43;
    /**
     * {@code fstore_1} ({@value FSTORE_1})
     *
     * @see NoOperandOpcode#FSTORE_1
     */
    public static final int FSTORE_1 = 0x44;
    /**
     * {@code fstore_2} ({@value FSTORE_2})
     *
     * @see NoOperandOpcode#FSTORE_2
     */
    public static final int FSTORE_2 = 0x45;
    /**
     * {@code fstore_3} ({@value FSTORE_3})
     *
     * @see NoOperandOpcode#FSTORE_3
     */
    public static final int FSTORE_3 = 0x46;
    /**
     * {@code fsub} ({@value FSUB})
     *
     * @see NoOperandOpcode#FSUB
     */
    public static final int FSUB = 0x66;
    /**
     * {@code getfield} ({@value GETFIELD})
     *
     * @see OneOperandOpcode#GETFIELD
     */
    public static final int GETFIELD = 0xb4;
    /**
     * {@code getstatic} ({@value GETSTATIC})
     *
     * @see OneOperandOpcode#GETSTATIC
     */
    public static final int GETSTATIC = 0xb2;
    /**
     * {@code goto} ({@value GOTO})
     *
     * @see OneOperandOpcode#GOTO
     */
    public static final int GOTO = 0xa7;
    /**
     * {@code goto_w} ({@value GOTO_W})
     *
     * @see OneOperandOpcode#GOTO_W
     */
    public static final int GOTO_W = 0xc8;
    /**
     * {@code i2b} ({@value I2B})
     *
     * @see NoOperandOpcode#I2B
     */
    public static final int I2B = 0x91;
    /**
     * {@code i2c} ({@value I2C})
     *
     * @see NoOperandOpcode#I2C
     */
    public static final int I2C = 0x92;
    /**
     * {@code i2d} ({@value I2D})
     *
     * @see NoOperandOpcode#I2D
     */
    public static final int I2D = 0x87;
    /**
     * {@code i2f} ({@value I2F})
     *
     * @see NoOperandOpcode#I2F
     */
    public static final int I2F = 0x86;
    /**
     * {@code i2l} ({@value I2L})
     *
     * @see NoOperandOpcode#I2L
     */
    public static final int I2L = 0x85;
    /**
     * {@code i2s} ({@value I2S})
     *
     * @see NoOperandOpcode#I2S
     */
    public static final int I2S = 0x93;
    /**
     * {@code iadd} ({@value IADD})
     *
     * @see NoOperandOpcode#IADD
     */
    public static final int IADD = 0x60;
    /**
     * {@code iaload} ({@value IALOAD})
     *
     * @see NoOperandOpcode#IALOAD
     */
    public static final int IALOAD = 0x2e;
    /**
     * {@code iand} ({@value IAND})
     *
     * @see NoOperandOpcode#IAND
     */
    public static final int IAND = 0x7e;
    /**
     * {@code iastore} ({@value IASTORE})
     *
     * @see NoOperandOpcode#IASTORE
     */
    public static final int IASTORE = 0x4f;
    /**
     * {@code iconst_m1} ({@value ICONST_M1})
     *
     * @see NoOperandOpcode#ICONST_M1
     */
    public static final int ICONST_M1 = 0x2;
    /**
     * {@code iconst_0} ({@value ICONST_0})
     *
     * @see NoOperandOpcode#ICONST_0
     */
    public static final int ICONST_0 = 0x3;
    /**
     * {@code iconst_1} ({@value ICONST_1})
     *
     * @see NoOperandOpcode#ICONST_1
     */
    public static final int ICONST_1 = 0x4;
    /**
     * {@code iconst_2} ({@value ICONST_2})
     *
     * @see NoOperandOpcode#ICONST_2
     */
    public static final int ICONST_2 = 0x5;
    /**
     * {@code iconst_3} ({@value ICONST_3})
     *
     * @see NoOperandOpcode#ICONST_3
     */
    public static final int ICONST_3 = 0x6;
    /**
     * {@code iconst_4} ({@value ICONST_4})
     *
     * @see NoOperandOpcode#ICONST_4
     */
    public static final int ICONST_4 = 0x7;
    /**
     * {@code iconst_5} ({@value ICONST_5})
     *
     * @see NoOperandOpcode#ICONST_5
     */
    public static final int ICONST_5 = 0x8;
    /**
     * {@code idiv} ({@value IDIV})
     *
     * @see NoOperandOpcode#IDIV
     */
    public static final int IDIV = 0x6c;
    /**
     * {@code if_acmpeq} ({@value IF_ACMPEQ})
     *
     * @see OneOperandOpcode#IF_ACMPEQ
     */
    public static final int IF_ACMPEQ = 0xa5;
    /**
     * {@code if_acmpne} ({@value IF_ACMPNE})
     *
     * @see OneOperandOpcode#IF_ACMPNE
     */
    public static final int IF_ACMPNE = 0xa6;
    /**
     * {@code if_icmpeq} ({@value IF_ICMPEQ})
     *
     * @see OneOperandOpcode#IF_ICMPEQ
     */
    public static final int IF_ICMPEQ = 0x9f;
    /**
     * {@code if_icmpge} ({@value IF_ICMPGE})
     *
     * @see OneOperandOpcode#IF_ICMPGE
     */
    public static final int IF_ICMPGE = 0xa2;
    /**
     * {@code if_icmpgt} ({@value IF_ICMPGT})
     *
     * @see OneOperandOpcode#IF_ICMPGT
     */
    public static final int IF_ICMPGT = 0xa3;
    /**
     * {@code if_icmple} ({@value IF_ICMPLE})
     *
     * @see OneOperandOpcode#IF_ICMPLE
     */
    public static final int IF_ICMPLE = 0xa4;
    /**
     * {@code if_icmplt} ({@value IF_ICMPLT})
     *
     * @see OneOperandOpcode#IF_ICMPLT
     */
    public static final int IF_ICMPLT = 0xa1;
    /**
     * {@code if_icmpne} ({@value IF_ICMPNE})
     *
     * @see OneOperandOpcode#IF_ICMPNE
     */
    public static final int IF_ICMPNE = 0xa0;
    /**
     * {@code ifeq} ({@value IFEQ})
     *
     * @see OneOperandOpcode#IFEQ
     */
    public static final int IFEQ = 0x99;
    /**
     * {@code ifge} ({@value IFGE})
     *
     * @see OneOperandOpcode#IFGE
     */
    public static final int IFGE = 0x9c;
    /**
     * {@code ifgt} ({@value IFGT})
     *
     * @see OneOperandOpcode#IFGT
     */
    public static final int IFGT = 0x9d;
    /**
     * {@code ifle} ({@value IFLE})
     *
     * @see OneOperandOpcode#IFLE
     */
    public static final int IFLE = 0x9e;
    /**
     * {@code iflt} ({@value IFLT})
     *
     * @see OneOperandOpcode#IFLT
     */
    public static final int IFLT = 0x9b;
    /**
     * {@code ifne} ({@value IFNE})
     *
     * @see OneOperandOpcode#IFNE
     */
    public static final int IFNE = 0x9a;
    /**
     * {@code ifnonnull} ({@value IFNONNULL})
     *
     * @see OneOperandOpcode#IFNONNULL
     */
    public static final int IFNONNULL = 0xc7;
    /**
     * {@code ifnull} ({@value IFNULL})
     *
     * @see OneOperandOpcode#IFNULL
     */
    public static final int IFNULL = 0xc6;
    /**
     * {@code iinc} ({@value IINC})
     *
     * @see TwoOperandOpcode#IINC
     */
    public static final int IINC = 0x84;
    /**
     * {@code iload} ({@value ILOAD})
     *
     * @see OneOperandOpcode#ILOAD
     */
    public static final int ILOAD = 0x15;
    /**
     * {@code iload_0} ({@value ILOAD_0})
     *
     * @see NoOperandOpcode#ILOAD_0
     */
    public static final int ILOAD_0 = 0x1a;
    /**
     * {@code iload_1} ({@value ILOAD_1})
     *
     * @see NoOperandOpcode#ILOAD_1
     */
    public static final int ILOAD_1 = 0x1b;
    /**
     * {@code iload_2} ({@value ILOAD_2})
     *
     * @see NoOperandOpcode#ILOAD_2
     */
    public static final int ILOAD_2 = 0x1c;
    /**
     * {@code iload_3} ({@value ILOAD_3})
     *
     * @see NoOperandOpcode#ILOAD_3
     */
    public static final int ILOAD_3 = 0x1d;
    /**
     * {@code impdep1} ({@value IMPDEP1})
     *
     * @see NoOperandOpcode#IMPDEP1
     */
    public static final int IMPDEP1 = 0xfe;
    /**
     * {@code impdep2} ({@value IMPDEP2})
     *
     * @see NoOperandOpcode#IMPDEP2
     */
    public static final int IMPDEP2 = 0xff;
    /**
     * {@code imul} ({@value IMUL})
     *
     * @see NoOperandOpcode#IMUL
     */
    public static final int IMUL = 0x68;
    /**
     * {@code ineg} ({@value INEG})
     *
     * @see NoOperandOpcode#INEG
     */
    public static final int INEG = 0x74;
    /**
     * {@code instanceof} ({@value INSTANCEOF})
     *
     * @see OneOperandOpcode#INSTANCEOF
     */
    public static final int INSTANCEOF = 0xc1;
    /**
     * {@code invokedynamic} ({@value INVOKEDYNAMIC})
     *
     * @see OneOperandOpcode#INVOKEDYNAMIC
     */
    public static final int INVOKEDYNAMIC = 0xba;
    /**
     * {@code invokeinterface} ({@value INVOKEINTERFACE})
     *
     * @see TwoOperandOpcode#INVOKEINTERFACE
     */
    public static final int INVOKEINTERFACE = 0xb9;
    /**
     * {@code invokespecial} ({@value INVOKESPECIAL})
     *
     * @see OneOperandOpcode#INVOKESPECIAL
     */
    public static final int INVOKESPECIAL = 0xb7;
    /**
     * {@code invokestatic} ({@value INVOKESTATIC})
     *
     * @see OneOperandOpcode#INVOKESTATIC
     */
    public static final int INVOKESTATIC = 0xb8;
    /**
     * {@code invokevirtual} ({@value INVOKEVIRTUAL})
     *
     * @see OneOperandOpcode#INVOKEVIRTUAL
     */
    public static final int INVOKEVIRTUAL = 0xb6;
    /**
     * {@code ior} ({@value IOR})
     *
     * @see NoOperandOpcode#IOR
     */
    public static final int IOR = 0x80;
    /**
     * {@code irem} ({@value IREM})
     *
     * @see NoOperandOpcode#IREM
     */
    public static final int IREM = 0x70;
    /**
     * {@code ireturn} ({@value IRETURN})
     *
     * @see NoOperandOpcode#IRETURN
     */
    public static final int IRETURN = 0xac;
    /**
     * {@code ishl} ({@value ISHL})
     *
     * @see NoOperandOpcode#ISHL
     */
    public static final int ISHL = 0x78;
    /**
     * {@code ishr} ({@value ISHR})
     *
     * @see NoOperandOpcode#ISHR
     */
    public static final int ISHR = 0x7a;
    /**
     * {@code istore} ({@value ISTORE})
     *
     * @see OneOperandOpcode#ISTORE
     */
    public static final int ISTORE = 0x36;
    /**
     * {@code istore_0} ({@value ISTORE_0})
     *
     * @see NoOperandOpcode#ISTORE_0
     */
    public static final int ISTORE_0 = 0x3b;
    /**
     * {@code istore_1} ({@value ISTORE_1})
     *
     * @see NoOperandOpcode#ISTORE_1
     */
    public static final int ISTORE_1 = 0x3c;
    /**
     * {@code istore_2} ({@value ISTORE_2})
     *
     * @see NoOperandOpcode#ISTORE_2
     */
    public static final int ISTORE_2 = 0x3d;
    /**
     * {@code istore_3} ({@value ISTORE_3})
     *
     * @see NoOperandOpcode#ISTORE_3
     */
    public static final int ISTORE_3 = 0x3e;
    /**
     * {@code isub} ({@value ISUB})
     *
     * @see NoOperandOpcode#ISUB
     */
    public static final int ISUB = 0x64;
    /**
     * {@code iushr} ({@value IUSHR})
     *
     * @see NoOperandOpcode#IUSHR
     */
    public static final int IUSHR = 0x7c;
    /**
     * {@code ixor} ({@value IXOR})
     *
     * @see NoOperandOpcode#IXOR
     */
    public static final int IXOR = 0x82;
    /**
     * {@code jsr} ({@value JSR})
     *
     * @see OneOperandOpcode#JSR
     */
    public static final int JSR = 0xa8;
    /**
     * {@code jsr_w} ({@value JSR_W})
     *
     * @see OneOperandOpcode#JSR_W
     */
    public static final int JSR_W = 0xc9;
    /**
     * {@code l2d} ({@value L2D})
     *
     * @see NoOperandOpcode#L2D
     */
    public static final int L2D = 0x8a;
    /**
     * {@code l2f} ({@value L2F})
     *
     * @see NoOperandOpcode#L2F
     */
    public static final int L2F = 0x89;
    /**
     * {@code l2i} ({@value L2I})
     *
     * @see NoOperandOpcode#L2I
     */
    public static final int L2I = 0x88;
    /**
     * {@code ladd} ({@value LADD})
     *
     * @see NoOperandOpcode#LADD
     */
    public static final int LADD = 0x61;
    /**
     * {@code laload} ({@value LALOAD})
     *
     * @see NoOperandOpcode#LALOAD
     */
    public static final int LALOAD = 0x2f;
    /**
     * {@code land} ({@value LAND})
     *
     * @see NoOperandOpcode#LAND
     */
    public static final int LAND = 0x7f;
    /**
     * {@code lastore} ({@value LASTORE})
     *
     * @see NoOperandOpcode#LASTORE
     */
    public static final int LASTORE = 0x50;
    /**
     * {@code lcmp} ({@value LCMP})
     *
     * @see NoOperandOpcode#LCMP
     */
    public static final int LCMP = 0x94;
    /**
     * {@code lconst_0} ({@value LCONST_0})
     *
     * @see NoOperandOpcode#LCONST_0
     */
    public static final int LCONST_0 = 0x9;
    /**
     * {@code lconst_1} ({@value LCONST_1})
     *
     * @see NoOperandOpcode#LCONST_1
     */
    public static final int LCONST_1 = 0x0a;
    /**
     * {@code ldc} ({@value LDC})
     *
     * @see OneOperandOpcode#LDC
     */
    public static final int LDC = 0x12;
    /**
     * {@code ldc_w} ({@value LDC_W})
     *
     * @see OneOperandOpcode#LDC_W
     */
    public static final int LDC_W = 0x13;
    /**
     * {@code ldc2_w} ({@value LDC2_W})
     *
     * @see OneOperandOpcode#LDC2_W
     */
    public static final int LDC2_W = 0x14;
    /**
     * {@code ldiv} ({@value LDIV})
     *
     * @see NoOperandOpcode#LDIV
     */
    public static final int LDIV = 0x6d;
    /**
     * {@code lload} ({@value LLOAD})
     *
     * @see OneOperandOpcode#LLOAD
     */
    public static final int LLOAD = 0x16;
    /**
     * {@code lload_0} ({@value LLOAD_0})
     *
     * @see NoOperandOpcode#LLOAD_0
     */
    public static final int LLOAD_0 = 0x1e;
    /**
     * {@code lload_1} ({@value LLOAD_1})
     *
     * @see NoOperandOpcode#LLOAD_1
     */
    public static final int LLOAD_1 = 0x1f;
    /**
     * {@code lload_2} ({@value LLOAD_2})
     *
     * @see NoOperandOpcode#LLOAD_2
     */
    public static final int LLOAD_2 = 0x20;
    /**
     * {@code lload_3} ({@value LLOAD_3})
     *
     * @see NoOperandOpcode#LLOAD_3
     */
    public static final int LLOAD_3 = 0x21;
    /**
     * {@code lmul} ({@value LMUL})
     *
     * @see NoOperandOpcode#LMUL
     */
    public static final int LMUL = 0x69;
    /**
     * {@code lneg} ({@value LNEG})
     *
     * @see NoOperandOpcode#LNEG
     */
    public static final int LNEG = 0x75;
    /**
     * {@code lookupswitch} ({@value LOOKUPSWITCH})
     *
     * @see VariableOperandOpcode#LOOKUPSWITCH
     */
    public static final int LOOKUPSWITCH = 0xab;
    /**
     * {@code lor} ({@value LOR})
     *
     * @see NoOperandOpcode#LOR
     */
    public static final int LOR = 0x81;
    /**
     * {@code lrem} ({@value LREM})
     *
     * @see NoOperandOpcode#LREM
     */
    public static final int LREM = 0x71;
    /**
     * {@code lreturn} ({@value LRETURN})
     *
     * @see NoOperandOpcode#LRETURN
     */
    public static final int LRETURN = 0xad;
    /**
     * {@code lshl} ({@value LSHL})
     *
     * @see NoOperandOpcode#LSHL
     */
    public static final int LSHL = 0x79;
    /**
     * {@code lshr} ({@value LSHR})
     *
     * @see NoOperandOpcode#LSHR
     */
    public static final int LSHR = 0x7b;
    /**
     * {@code lstore} ({@value LSTORE})
     *
     * @see OneOperandOpcode#LSTORE
     */
    public static final int LSTORE = 0x37;
    /**
     * {@code lstore_0} ({@value LSTORE_0})
     *
     * @see NoOperandOpcode#LSTORE_0
     */
    public static final int LSTORE_0 = 0x3f;
    /**
     * {@code lstore_1} ({@value LSTORE_1})
     *
     * @see NoOperandOpcode#LSTORE_1
     */
    public static final int LSTORE_1 = 0x40;
    /**
     * {@code lstore_2} ({@value LSTORE_2})
     *
     * @see NoOperandOpcode#LSTORE_2
     */
    public static final int LSTORE_2 = 0x41;
    /**
     * {@code lstore_3} ({@value LSTORE_3})
     *
     * @see NoOperandOpcode#LSTORE_3
     */
    public static final int LSTORE_3 = 0x42;
    /**
     * {@code lsub} ({@value LSUB})
     *
     * @see NoOperandOpcode#LSUB
     */
    public static final int LSUB = 0x65;
    /**
     * {@code lushr} ({@value LUSHR})
     *
     * @see NoOperandOpcode#LUSHR
     */
    public static final int LUSHR = 0x7d;
    /**
     * {@code lxor} ({@value LXOR})
     *
     * @see NoOperandOpcode#LXOR
     */
    public static final int LXOR = 0x83;
    /**
     * {@code monitorenter} ({@value MONITORENTER})
     *
     * @see NoOperandOpcode#MONITORENTER
     */
    public static final int MONITORENTER = 0xc2;
    /**
     * {@code monitorexit} ({@value MONITOREXIT})
     *
     * @see NoOperandOpcode#MONITOREXIT
     */
    public static final int MONITOREXIT = 0xc3;
    /**
     * {@code multianewarray} ({@value MULTIANEWARRAY})
     *
     * @see TwoOperandOpcode#MULTIANEWARRAY
     */
    public static final int MULTIANEWARRAY = 0xc5;
    /**
     * {@code new} ({@value NEW})
     *
     * @see OneOperandOpcode#NEW
     */
    public static final int NEW = 0xbb;
    /**
     * {@code newarray} ({@value NEWARRAY})
     *
     * @see OneOperandOpcode#NEWARRAY
     */
    public static final int NEWARRAY = 0xbc;
    /**
     * {@code nop} ({@value NOP})
     *
     * @see NoOperandOpcode#NOP
     */
    public static final int NOP = 0x0;
    /**
     * {@code pop} ({@value POP})
     *
     * @see NoOperandOpcode#POP
     */
    public static final int POP = 0x57;
    /**
     * {@code pop2} ({@value POP2})
     *
     * @see NoOperandOpcode#POP2
     */
    public static final int POP2 = 0x58;
    /**
     * {@code putfield} ({@value PUTFIELD})
     *
     * @see OneOperandOpcode#PUTFIELD
     */
    public static final int PUTFIELD = 0xb5;
    /**
     * {@code putstatic} ({@value PUTSTATIC})
     *
     * @see OneOperandOpcode#PUTSTATIC
     */
    public static final int PUTSTATIC = 0xb3;
    /**
     * {@code ret} ({@value RET})
     *
     * @see OneOperandOpcode#RET
     */
    public static final int RET = 0xa9;
    /**
     * {@code return} ({@value RETURN})
     *
     * @see NoOperandOpcode#RETURN
     */
    public static final int RETURN = 0xb1;
    /**
     * {@code saload} ({@value SALOAD})
     *
     * @see NoOperandOpcode#SALOAD
     */
    public static final int SALOAD = 0x35;
    /**
     * {@code sastore} ({@value SASTORE})
     *
     * @see NoOperandOpcode#SASTORE
     */
    public static final int SASTORE = 0x56;
    /**
     * {@code sipush} ({@value SIPUSH})
     *
     * @see OneOperandOpcode#SIPUSH
     */
    public static final int SIPUSH = 0x11;
    /**
     * {@code swap} ({@value SWAP})
     *
     * @see NoOperandOpcode#SWAP
     */
    public static final int SWAP = 0x5f;
    /**
     * {@code tableswitch} ({@value TABLESWITCH})
     *
     * @see VariableOperandOpcode#TABLESWITCH
     */
    public static final int TABLESWITCH = 0xaa;
    /**
     * {@code wide} ({@value WIDE})
     *
     * @see VariableOperandOpcode#WIDE
     */
    public static final int WIDE = 0xc4;

    //
    // CONSTRUCTORS
    //

    private OpcodeValue() {
    }
}
