; ModuleID = 'builtin.c'
source_filename = "builtin.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

@.str = private unnamed_addr constant [3 x i8] c"%s\00", align 1
@.str.2 = private unnamed_addr constant [3 x i8] c"%d\00", align 1
@.str.3 = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1

; Function Attrs: nofree nounwind uwtable
define dso_local void @print(i8*) local_unnamed_addr #0 {
  %2 = tail call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i64 0, i64 0), i8* %0)
  ret void
}

; Function Attrs: nofree nounwind
declare dso_local i32 @printf(i8* nocapture readonly, ...) local_unnamed_addr #1

; Function Attrs: nofree nounwind uwtable
define dso_local void @println(i8* nocapture readonly) local_unnamed_addr #0 {
  %2 = tail call i32 @puts(i8* %0)
  ret void
}

; Function Attrs: nofree nounwind uwtable
define dso_local void @printInt(i32) local_unnamed_addr #0 {
  %2 = tail call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.2, i64 0, i64 0), i32 %0)
  ret void
}

; Function Attrs: nofree nounwind uwtable
define dso_local void @printlnInt(i32) local_unnamed_addr #0 {
  %2 = tail call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.3, i64 0, i64 0), i32 %0)
  ret void
}

; Function Attrs: nofree nounwind uwtable
define dso_local i8* @getString() local_unnamed_addr #0 {
  %1 = tail call noalias i8* @malloc(i64 1000) #9
  %2 = tail call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str, i64 0, i64 0), i8* %1)
  ret i8* %1
}

; Function Attrs: argmemonly nounwind
declare void @llvm.lifetime.start.p0i8(i64 immarg, i8* nocapture) #2

; Function Attrs: nofree nounwind
declare dso_local noalias i8* @malloc(i64) local_unnamed_addr #1

; Function Attrs: nofree nounwind
declare dso_local i32 @__isoc99_scanf(i8* nocapture readonly, ...) local_unnamed_addr #1

; Function Attrs: argmemonly nounwind
declare void @llvm.lifetime.end.p0i8(i64 immarg, i8* nocapture) #2

; Function Attrs: nounwind uwtable
define dso_local i32 @getInt() local_unnamed_addr #3 {
  %1 = alloca i32, align 4
  %2 = bitcast i32* %1 to i8*
  call void @llvm.lifetime.start.p0i8(i64 4, i8* nonnull %2) #9
  %3 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.2, i64 0, i64 0), i32* nonnull %1)
  %4 = load i32, i32* %1, align 4, !tbaa !2
  call void @llvm.lifetime.end.p0i8(i64 4, i8* nonnull %2) #9
  ret i32 %4
}

; Function Attrs: nofree nounwind uwtable
define dso_local noalias i8* @toString(i32) local_unnamed_addr #0 {
  %2 = icmp eq i32 %0, 0
  br i1 %2, label %3, label %6

3:                                                ; preds = %1
  %4 = tail call noalias i8* @malloc(i64 2) #9
  store i8 48, i8* %4, align 1, !tbaa !6
  %5 = getelementptr inbounds i8, i8* %4, i64 1
  br label %42

6:                                                ; preds = %1
  %7 = tail call noalias i8* @malloc(i64 12) #9
  %8 = icmp slt i32 %0, 0
  br i1 %8, label %9, label %11

9:                                                ; preds = %6
  store i8 45, i8* %7, align 1, !tbaa !6
  %10 = sub nsw i32 0, %0
  br label %11

11:                                               ; preds = %9, %6
  %12 = phi i32 [ %10, %9 ], [ %0, %6 ]
  %13 = phi i32 [ 1, %9 ], [ 0, %6 ]
  %14 = icmp slt i32 %12, 1000000000
  br i1 %14, label %20, label %17

15:                                               ; preds = %20
  %16 = icmp sgt i32 %21, 9
  br i1 %16, label %17, label %38

17:                                               ; preds = %11, %15
  %18 = phi i32 [ %22, %15 ], [ 1000000000, %11 ]
  %19 = zext i32 %13 to i64
  br label %24

20:                                               ; preds = %11, %20
  %21 = phi i32 [ %22, %20 ], [ 1000000000, %11 ]
  %22 = sdiv i32 %21, 10
  %23 = icmp sgt i32 %22, %12
  br i1 %23, label %20, label %15

24:                                               ; preds = %17, %24
  %25 = phi i64 [ %19, %17 ], [ %31, %24 ]
  %26 = phi i32 [ %18, %17 ], [ %34, %24 ]
  %27 = phi i32 [ %12, %17 ], [ %33, %24 ]
  %28 = sdiv i32 %27, %26
  %29 = trunc i32 %28 to i8
  %30 = add i8 %29, 48
  %31 = add nuw i64 %25, 1
  %32 = getelementptr inbounds i8, i8* %7, i64 %25
  store i8 %30, i8* %32, align 1, !tbaa !6
  %33 = srem i32 %27, %26
  %34 = udiv i32 %26, 10
  %35 = icmp ugt i32 %26, 9
  br i1 %35, label %24, label %36

36:                                               ; preds = %24
  %37 = trunc i64 %31 to i32
  br label %38

38:                                               ; preds = %36, %15
  %39 = phi i32 [ %13, %15 ], [ %37, %36 ]
  %40 = zext i32 %39 to i64
  %41 = getelementptr inbounds i8, i8* %7, i64 %40
  br label %42

42:                                               ; preds = %38, %3
  %43 = phi i8* [ %41, %38 ], [ %5, %3 ]
  %44 = phi i8* [ %7, %38 ], [ %4, %3 ]
  store i8 0, i8* %43, align 1, !tbaa !6
  ret i8* %44
}

; Function Attrs: nounwind readonly uwtable
define dso_local i32 @string_length(i8* nocapture readonly) local_unnamed_addr #4 {
  %2 = tail call i64 @strlen(i8* %0) #10
  %3 = trunc i64 %2 to i32
  ret i32 %3
}

; Function Attrs: argmemonly nofree nounwind readonly
declare dso_local i64 @strlen(i8* nocapture) local_unnamed_addr #5

; Function Attrs: nounwind uwtable
define dso_local noalias i8* @string_substring(i8* nocapture readonly, i32, i32) local_unnamed_addr #3 {
  %4 = sub nsw i32 %2, %1
  %5 = add nsw i32 %4, 1
  %6 = sext i32 %5 to i64
  %7 = tail call noalias i8* @malloc(i64 %6) #9
  %8 = sext i32 %1 to i64
  %9 = getelementptr inbounds i8, i8* %0, i64 %8
  %10 = sext i32 %4 to i64
  tail call void @llvm.memcpy.p0i8.p0i8.i64(i8* align 1 %7, i8* align 1 %9, i64 %10, i1 false)
  %11 = getelementptr inbounds i8, i8* %7, i64 %10
  store i8 0, i8* %11, align 1, !tbaa !6
  ret i8* %7
}

; Function Attrs: argmemonly nounwind
declare void @llvm.memcpy.p0i8.p0i8.i64(i8* nocapture writeonly, i8* nocapture readonly, i64, i1 immarg) #2

; Function Attrs: nounwind uwtable
define dso_local i32 @string_parseInt(i8* nocapture readonly) local_unnamed_addr #3 {
  %2 = alloca i32, align 4
  %3 = bitcast i32* %2 to i8*
  call void @llvm.lifetime.start.p0i8(i64 4, i8* nonnull %3) #9
  %4 = call i32 (i8*, i8*, ...) @__isoc99_sscanf(i8* %0, i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.str.2, i64 0, i64 0), i32* nonnull %2) #9
  %5 = load i32, i32* %2, align 4, !tbaa !2
  call void @llvm.lifetime.end.p0i8(i64 4, i8* nonnull %3) #9
  ret i32 %5
}

; Function Attrs: nofree nounwind
declare dso_local i32 @__isoc99_sscanf(i8* nocapture readonly, i8* nocapture readonly, ...) local_unnamed_addr #1

; Function Attrs: norecurse nounwind readonly uwtable
define dso_local i32 @string_ord(i8* nocapture readonly, i32) local_unnamed_addr #6 {
  %3 = sext i32 %1 to i64
  %4 = getelementptr inbounds i8, i8* %0, i64 %3
  %5 = load i8, i8* %4, align 1, !tbaa !6
  %6 = sext i8 %5 to i32
  ret i32 %6
}

; Function Attrs: nofree nounwind uwtable
define dso_local i8* @string_add(i8* nocapture readonly, i8* nocapture readonly) local_unnamed_addr #0 {
  %3 = tail call i64 @strlen(i8* %0) #10
  %4 = tail call i64 @strlen(i8* %1) #10
  %5 = add i64 %4, %3
  %6 = shl i64 %5, 32
  %7 = add i64 %6, 4294967296
  %8 = ashr exact i64 %7, 32
  %9 = tail call noalias i8* @malloc(i64 %8) #9
  %10 = tail call i8* @strcpy(i8* %9, i8* %0) #9
  %11 = tail call i8* @strcat(i8* %9, i8* %1) #9
  ret i8* %9
}

; Function Attrs: nofree nounwind
declare dso_local i8* @strcpy(i8* returned, i8* nocapture readonly) local_unnamed_addr #1

; Function Attrs: nofree nounwind
declare dso_local i8* @strcat(i8* returned, i8* nocapture readonly) local_unnamed_addr #1

; Function Attrs: nounwind readonly uwtable
define dso_local i32 @string_eq(i8* nocapture readonly, i8* nocapture readonly) local_unnamed_addr #4 {
  %3 = tail call i32 @strcmp(i8* %0, i8* %1) #10
  %4 = icmp eq i32 %3, 0
  %5 = zext i1 %4 to i32
  ret i32 %5
}

; Function Attrs: nofree nounwind readonly
declare dso_local i32 @strcmp(i8* nocapture, i8* nocapture) local_unnamed_addr #7

; Function Attrs: nounwind readonly uwtable
define dso_local i32 @string_neq(i8* nocapture readonly, i8* nocapture readonly) local_unnamed_addr #4 {
  %3 = tail call i32 @strcmp(i8* %0, i8* %1) #10
  %4 = icmp ne i32 %3, 0
  %5 = zext i1 %4 to i32
  ret i32 %5
}

; Function Attrs: nounwind readonly uwtable
define dso_local i32 @string_lt(i8* nocapture readonly, i8* nocapture readonly) local_unnamed_addr #4 {
  %3 = tail call i32 @strcmp(i8* %0, i8* %1) #10
  %4 = lshr i32 %3, 31
  ret i32 %4
}

; Function Attrs: nounwind readonly uwtable
define dso_local i32 @string_le(i8* nocapture readonly, i8* nocapture readonly) local_unnamed_addr #4 {
  %3 = tail call i32 @strcmp(i8* %0, i8* %1) #10
  %4 = icmp slt i32 %3, 1
  %5 = zext i1 %4 to i32
  ret i32 %5
}

; Function Attrs: nounwind readonly uwtable
define dso_local i32 @string_gt(i8* nocapture readonly, i8* nocapture readonly) local_unnamed_addr #4 {
  %3 = tail call i32 @strcmp(i8* %0, i8* %1) #10
  %4 = icmp sgt i32 %3, 0
  %5 = zext i1 %4 to i32
  ret i32 %5
}

; Function Attrs: nounwind readonly uwtable
define dso_local i32 @string_ge(i8* nocapture readonly, i8* nocapture readonly) local_unnamed_addr #4 {
  %3 = tail call i32 @strcmp(i8* %0, i8* %1) #10
  %4 = lshr i32 %3, 31
  %5 = xor i32 %4, 1
  ret i32 %5
}

; Function Attrs: nofree nounwind
declare i32 @puts(i8* nocapture readonly) local_unnamed_addr #8

attributes #0 = { nofree nounwind uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #1 = { nofree nounwind "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #2 = { argmemonly nounwind }
attributes #3 = { nounwind uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #4 = { nounwind readonly uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #5 = { argmemonly nofree nounwind readonly "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #6 = { norecurse nounwind readonly uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #7 = { nofree nounwind readonly "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #8 = { nofree nounwind }
attributes #9 = { nounwind }
attributes #10 = { nounwind readonly }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 9.0.0-2 (tags/RELEASE_900/final)"}
!2 = !{!3, !3, i64 0}
!3 = !{!"int", !4, i64 0}
!4 = !{!"omnipotent char", !5, i64 0}
!5 = !{!"Simple C/C++ TBAA"}
!6 = !{!4, !4, i64 0}
