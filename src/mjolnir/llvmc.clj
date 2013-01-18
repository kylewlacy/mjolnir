(ns mjolnir.llvmc
  (:import (com.sun.jna Native Pointer Memory))
  (:require [clojure.java.shell :as shell]
            [clojure.string :as string]))

(def ^:dynamic *lib* 'LLVM-3.3svn)

(def strip-chars 4)

(defn get-function [s]
  `(com.sun.jna.Function/getFunction ~(name *lib*) ~(name s)))

(defn debug [s]
  (println s)
  s)

(def debug-mode false)

(defmacro defnative
  [return-type function-symbol]
  `(let [func# ~(get-function function-symbol)]
     (defn ~(symbol (apply str (drop strip-chars (name function-symbol))))
       [& args#]
       (let [r# (.invoke func# ~return-type (to-array args#))]
         (when debug-mode
           (println "After " ~(name function-symbol))
           (System/gc)
           (System/runFinalization)
           (Thread/sleep 500))
         r#))))

(defn new-pointer []
  (let [p (Memory. Pointer/SIZE)]
   (.clear p)
    p))


(defn to-pointers [& args]
  (let [arr (make-array Pointer (count args))]
    (loop [a args
           c 0]
      (if a
        (do (aset arr c (first a))
            (recur (next a) (inc c)))
        arr))))


(def LLVMCCallConv 0)
(def LLVMFastCallConv 8)
(def LLVMColdCallConv 9)
(def LLVMX86StdcallCallConv 64)
(def LLVMX86FastcallCallConv 65)
(defnative Integer LLVMSetFunctionCallConv)
(defnative Integer LLVMFindFunction)

(defnative Pointer LLVMAppendBasicBlock)
(defnative Pointer LLVMCreateBuilder)

(defnative Pointer LLVMGetParam)

(defnative Integer LLVMLinkInJIT)
'(defnative Integer LLVMInitializeNativeTarget)

(defnative Pointer LLVMModuleCreateWithName)

(defnative Pointer LLVMInt32Type)
(defnative Pointer LLVMFunctionType)

(defnative Pointer LLVMAddFunction)

(defnative Integer LLVMPositionBuilderAtEnd)

(defnative Boolean LLVMVerifyModule)

(def LLVMAbortProcessAction 0)
(def LLVMPrintMessageAction 1)
(def LLVMReturnStatusAction 2)

(defnative Pointer LLVMCreateModuleProviderForExistingModule)

(defnative Integer LLVMDisposeMessage)
(defnative Integer LLVMCreateJITCompiler)
(defnative Integer LLVMCreateInterpreterForModule)
(defnative Pointer LLVMCreatePassManager)
(defnative Pointer LLVMGetExecutionEngineTargetData)
(defnative Integer LLVMAddTargetData)
(defnative Integer LLVMRunPassManager)
(defnative Integer LLVMDumpModule)
(defnative Integer LLVMDisposePassManager)
(defnative Integer LLVMDisposeExecutionEngine)
(defnative Integer LLVMBuildRet)

(defnative Integer LLVMLinkInJIT)
(defnative Integer LLVMLinkInInterpreter)
(defnative Integer LLVMInitializeX86Target)
(defnative Integer LLVMInitializeX86TargetInfo)
(defnative Integer LLVMInitializeX86TargetMC)
(defnative Pointer LLVMRunFunction)
(defnative Boolean LLVMFindFunction)
(defnative Pointer LLVMCreateGenericValueOfInt)
(defnative Integer LLVMGenericValueToInt)
(defnative Pointer LLVMBuildAdd)
(defnative Pointer LLVMBuildSub)
(defnative Pointer LLVMConstInt)
(defnative Pointer LLVMBuildICmp)
(defnative Pointer LLVMIntType)
(defnative Pointer LLVMBuildCondBr)
(defnative Pointer LLVMBuildPhi)
(defnative Integer LLVMAddIncoming)
(defnative Pointer LLVMTypeOf)
(defnative Integer LLVMCountParamTypes)
(defnative Integer LLVMGetTypeKind)
(defnative Integer LLVMDisposeGenericValue)
(defnative Integer LLVMDisposeBuilder)
(defnative Pointer LLVMBuildBr)
(defnative Pointer LLVMBuildCall)
(defnative Pointer LLVMBuildAlloca)
(defnative Pointer LLVMBuildFree)
(defnative Pointer LLVMBuildLoad)
(defnative Pointer LLVMBuildStore)

(defnative Integer LLVMAddConstantPropagationPass)
(defnative Integer LLVMAddInstructionCombiningPass)
(defnative Integer LLVMAddPromoteMemoryToRegisterPass)
(defnative Integer LLVMAddGVNPass)
(defnative Integer LLVMAddCFGSimplificationPass)
(defnative Pointer LLVMBuildArrayMalloc)
(defnative Pointer LLVMBuildGEP)
(defnative Pointer LLVMBuildBitCast)
(defnative Pointer LLVMConstString)
(defnative Pointer LLVMConstInt)
(defnative Integer LLVMCountStructElementTypes)
(defnative Pointer LLVMConstPointerCast)
(defnative Pointer LLVMGetStructElementTypes)
(defnative Integer LLVMGetTypeKind)
(defnative Pointer LLVMConstPointerNull)
(defnative Pointer LLVMInt64Type)
(defnative Pointer LLVMStructType)
(defnative Pointer LLVMArrayType)
(defnative Pointer LLVMDumpValue)
(defnative Integer LLVMGetArrayLength)
(defnative Pointer LLVMGetElementType)
(defnative Pointer LLVMConstArray)
(defnative Pointer LLVMConstString)
(defnative Pointer LLVMConstStruct)
(defnative Pointer LLVMConstGEP)
(defnative Pointer LLVMConstBitCast)
(defnative Integer LLVMCountParams)
(defnative Pointer LLVMAddGlobal)
(defnative Integer LLVMSetInitializer)
(defnative Integer LLVMWriteBitcodeToFile)
(defnative Pointer LLVMGetNamedGlobal)
(defnative Pointer LLVMGetNamedFunction)
(defnative Pointer LLVMInt8Type)
(defnative Pointer LLVMInt1Type)
(defnative Pointer LLVMFloatType)
(defnative Pointer LLVMPointerType)
(defnative Integer LLVMSetLinkage)
(defnative Integer LLVMGetIntTypeWidth)
(defnative Pointer LLVMBuildStructGEP)
(defnative Pointer LLVMBuildAdd)
(defnative Pointer LLVMBuildSub)
(defnative Pointer LLVMBuildShl)
(defnative Pointer LLVMBuildLShr)
(defnative Pointer LLVMBuildAnd)
(defnative Pointer LLVMBuildOr)
(defnative Pointer LLVMBuildMalloc)

(def ^:dynamic *module* (ModuleCreateWithName "tmp"))
(def ^:dynamic *fn*)
(def ^:dynamic *locals*)
(def ^:dynamic *builder*)
(def ^:dynamic *block*)

(defn init-target []
  (LinkInJIT)
  (LinkInInterpreter)
  (InitializeX86TargetInfo)
  (InitializeX86Target)
  (InitializeX86TargetMC))

(init-target)

(def LLVMIntEQ 32)

(defmacro defenum
  [nm defs]
  (list* 'do
        `(def ~nm {:idx ~(zipmap (range)
                                   (map (comp keyword name) defs))
                     :defs ~(zipmap (map (comp keyword name) defs)
                                    (range))})
        (map-indexed (fn [idx d]
                       `(def ~d ~idx))
                     defs)))

(defenum LLVMTypeKind
  [LLVMVoidTypeKind
   LLVMHalfTypeKind
   LLVMFloatTypeKind
   LLVMDoubleTypeKind
   LLVMX86_FP80TypeKind
   LLVMFP128TypeKind
   LLVMPPC_FP128TypeKind
   LLVMLabelTypeKind
   LLVMIntegerTypeKind
   LLVMFunctionTypeKind
   LLVMStructTypeKind
   LLVMArrayTypeKind
   LLVMPointerTypeKind
   LLVMVectorTypeKind
   LLVMMetadataTypeKind
   LLVMX86_MMXTypeKind])

(defenum LLVMCodeGentFileType
  [LLVMAssemblyFile
   LLVMObjectFile])

(defenum LLVMRelocMode
  [LLVMRelocDefault
   LLVMRelocStatic
   LLVMRelocPIC
   LLVMRelocDynamicNoPIC])

(defenum LLVMCodeGenOptLevel
  [LLVMCodeGenLevelNone
   LLVMCodeGenLevelLess
   LLVMCodeGenLevelDefault
   LLVMCodeGenLevelAggressive])

(defenum LLVMCodeModel
  [LLVMCodeModelDefault
   LLVMCodeModelJITDefault
   LLVMCodeModelSmall
   LLVMCodeModelKernel
   LLVMCodeModelMedium
   LLVMCodeModelLarge])


(defenum LLVMLinkage
  [LLVMExternalLinkage,    ; Externally visible function 
   LLVMAvailableExternallyLinkage,
   LLVMLinkOnceAnyLinkage, ; Keep one copy of function when linking (inline)
   LLVMLinkOnceODRLinkage, ; Same, but only replaced by something equivalent. 
   LLVMWeakAnyLinkage,     ; Keep one copy of function when linking (weak) 
   LLVMWeakODRLinkage,     ; Same, but only replaced by something equivalent. 
   LLVMAppendingLinkage,   ; Special purpose, only applies to global arrays 
   LLVMInternalLinkage,    ; Rename collisions when linking (static functions)
   LLVMPrivateLinkage,     ; Like Internal, but omit from symbol table 
   LLVMDLLImportLinkage,   ; Function to be imported from DLL 
   LLVMDLLExportLinkage,   ; Function to be accessible from DLL 
   LLVMExternalWeakLinkage,; ExternalWeak linkage description 
   LLVMGhostLinkage,       ; Obsolete 
   LLVMCommonLinkage,      ; Tentative definitions 
   LLVMLinkerPrivateLinkage, ; Like Private, but linker removes. 
   LLVMLinkerPrivateWeakLinkage, ; Like LinkerPrivate, but is weak. 
   LLVMLinkerPrivateWeakDefAutoLinkage]) ; Like LinkerPrivateWeak, but possibly hidden. 


(defn init-target []
  (LinkInJIT)
  (LinkInInterpreter)
  (InitializeX86TargetInfo)
  (InitializeX86Target)
  (InitializeX86TargetMC))

(def CCallConv 0)
(def FastCallConv 8)
(def ColdCallConv 9)
(def X86StdcallCallConv 64)
(def X86FastcallCallConv 65)

(def AbortProcessAction 0)
(def PrintMessageAction 1)
(def ReturnStatusAction 2)


(defn map-parr [fn coll]
  (into-array Pointer
              (map fn coll)))

(defn value-at [ptr]
  (.getPointer ptr 0))

(def kw->linkage
  {:extern LLVMExternalLinkage})