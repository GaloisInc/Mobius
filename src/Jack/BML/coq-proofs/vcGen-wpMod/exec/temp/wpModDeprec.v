Require Import semantiqueAux1. 
Require Import List.
Require Import ZArith.

(*COMMENT : this version is not correct. The definition of wp requires that in the postconditio of the body of a while false holds ! *)
 
(*creates a state which corresponds to the state at the loop borders and takes into account the modified variables *)
Definition stateAtLoop (state : State) (stateAux : AuxState )( z: Z) (lVar : list Var )  : State := 
                                                                         fun var: Var => 
                                                                            if  ( In_dec  varEqDec   var lVar ) 
                                                                                 then (  stateAux  ( AuxName var z ))
                                                                                 else  state var.

 (*creates a state which corresponds to the state at the loop borders and takes into account the modified variables *)
Definition FreezeVarAtIterStart 
(state : State) (stateAux : AuxState )( z: Z) (lVar :list Var): State := 
          fun var: Var => if ( In_dec varEqDec var lVar) 
                                        then (state var) 
                                        else ( stateAux  ( AuxName var z )).
                                                                                 


Reserved Notation "'wpMod' ( a , post )  = pre" (at level 30).
Inductive is_wpMod : stmt_m -> Assertion -> Assertion -> Prop :=
| is_wpMod_caseSkip : forall post z,  wpMod(( SkipInd  Invariant_m z), post) = post
| is_wpMod_caseAffect :   forall post var expr z, 
  wpMod((AffectInd  Invariant_m z var expr), post)  = (update_assert post var expr ) 
| is_wpMod_caseIf : forall (post : Assertion ) (pre_t : Assertion )(pre_f : Assertion) st1 st2 bExp z,
  wpMod(st2, post) = pre_f  ->
    wpMod(st1, post) = pre_t ->
      wpMod((IfInd  Invariant_m z bExp st1 st2), post) =
	(fun ( st : State *AuxState ) =>   
          let (s , sAux) := st in
	  p_and (p_implies (p_neq ( neval s sAux  bExp  ) 0) (pre_t st ))
	   	    (p_implies (p_eq ( neval s  sAux bExp  ) 0)     (pre_f st) ) ) 
| is_wpMod_caseWhile : forall post pre_st invariant varNotModif lVar bExp stmt z , 
	(*   ( isFresh z
          (fun st : State * AuxState =>
          let (s, sAux) := st in
          p_implies 
                (p_and (p_forallv (fun x => (p_implies 
                                                                  (p_not (p_in x lVar)) 
                                                                  (p_eq (sAux  ( AuxName x z   )) ( s  x )))))  
                (p_and (invariant (s, sAux)) (p_neq (neval s sAux bExp ) 0)))
          (pre_st (s, sAux))))  ->  
          
          (isFresh z     (fun st : State * AuxState =>
                  let (stProg, sAux) := st in
                   ( p_and  ( varNotModif  ((FreezeVarAtIterStart  stProg sAux z  ), sAux ) )
                   (p_and
                      (p_foralls (fun s  =>  (p_implies
                           (p_and (p_forallv (fun x => (p_implies 
                                                                  (p_not (p_in x lVar)) 
                                                                  (p_eq (stProg x) ( s  x )))))  
                          (p_and (invariant ( s, sAux) ) 
                                       (p_neq (neval s  sAux bExp) 0)))
                          (pre_st ( s , sAux) ))))
                       (p_foralls (fun s=> ( p_implies
                           (p_and (p_forallv (fun x => (p_implies 
                                                                  (p_not (p_in x lVar)) 
                                                                  (p_eq (stProg x ) ( s  x )))))  
                          (p_and (invariant (s, sAux) ) (p_eq (neval s sAux bExp) 0)))
                          (post (s, sAux) ))))))))   ->  


       ( isFresh z
    (fun st : State * AuxState =>
     let (s, sAux) := st in
     p_implies
       (p_and (p_forallv (fun x => (p_implies 
                                                                  (p_not (p_in x lVar)) 
                                                                  (p_eq (sAux  ( AuxName x z   )) ( s  x )))))  
       (p_and (invariant (s, sAux)) (p_eq (neval s sAux bExp ) 0)))
       (post (s, sAux)))) -> 
      (* let  stateWithMod := ( stateAtLoop  state z lVar )   in *)
*)
forall STAUX,
( wpMod(stmt ,   fun st   => let (s, sAux) := st in
                                             (p_forallv (fun x => (p_implies 
                                                                                (p_not (p_in x lVar)) 
                                                                                (p_eq (STAUX  ( AuxName x z   )) ( s  x ))))) ) = varNotModif  ) -> 
          
 ( wpMod(stmt , invariant  ) = pre_st  ) -> 
       wpMod((WhileInd Invariant_m  z bExp   (inv_m invariant lVar ) stmt ), post) =
       fun st => 
              let (s , sAux) := st in
               p_and 
              ( invariant st )
              (*  p_forallsAux   fun sAux1 => *)
		(p_and  (  (p_forallv (fun x => ((p_implies 
                                   (p_not (p_in x lVar)) 
                                  (p_eq (STAUX  ( AuxName x z   )) ( s  x )))))))
               ( p_and ( p_foralls ( fun s => ( varNotModif  ((FreezeVarAtIterStart  s STAUX z lVar ), STAUX ) )))
                  ( p_and                         
                   (p_foralls ( fun s => ( 
                  
	          (* the invariant is preserved*)
                    (p_implies 
                        (p_forallv (fun x => (p_implies  (* every variable var not declared in the list lVar must be equal at every start iteration state to the values the var had in the state stProg just before the loop started  *)
                                                          (p_not (p_in x lVar)) 
                                                          (p_eq (STAUX ( AuxName x z ) ) ( s  x )))))       
                        (p_implies  ( invariant ( s ,  sAux )  ) 
                            ( p_implies  (p_neq (neval  s sAux   bExp)  0)
		                 ( pre_st ( s, sAux))))))))
				(*the postcondition at the end of the loop is implied *)
                   (p_foralls ( fun s => ( 
		   
                   ( p_implies  (p_forallv (fun x => (p_implies 
                                                                           (p_not (p_in x lVar)) 
                                                                           (p_eq (STAUX ( AuxName x z ) ) ( s  x )))))
                           ( p_implies  ( invariant ( s , sAux ) ) 
                              ( p_implies                
                                    (p_eq (neval  s sAux   bExp)  0)
                                    	( post (s , sAux)))))))))))
| is_wpMod_caseSeq : forall post pre_st2 pre_st1 st1 st2 z,
      ( wpMod(st2, post) = pre_st2) -> 
        (wpMod(st1, pre_st2) = pre_st1) ->
          (wpMod(( SeqInd Invariant_m z st1 st2 ), post) = pre_st1)
where "'wpMod' ( a , post )  = pre"  := (is_wpMod a post pre).


(*Lemma notMod : forall stmt st1 st2 lVar auxState varNotMod z,
( execBs _ st1 stmt st2)   ->
 wpMod (
  stmt ,
  ( fun st   => let (s, sAux) := st in
                          (p_forallv (fun x => (p_implies 
                                                    (p_not (p_in x lVar)) 
                                                    (p_eq (sAux  ( AuxName x z   )) ( s  x ))))) )) = varNotMod 

 -> 
 evalMyProp ( varNotMod  ((FreezeVarAtIterStart  st1 auxState z  ), auxState))
 -> 
forall var , ~ In var lVar ->  (st1 var) = (st2 var).
Proof.
intros until z; 
intros exec; induction exec;
intro  wpModRel ;
intro varNotModH.
trivial.
unfold update.
unfold FreezeVarAtIterStart in *.
unfold vareq.
Qed. *)
 
Axiom semAux : 
forall state aState ( P :  Assertion )  lVar  z,
 (isFresh  z P) -> 
evalMyProp ( p_forallsAux  ( fun auxState => P ( ( stateAtLoop state auxState z lVar )  , auxState)))  ->
 (evalMyProp ( P ( state, aState ))).
(* Proof.
intros.
simpl in *.
unfold stateAtLoop in *.
assert (H1 :=  H  aState).
clear H.
unfold If in *.
Qed.  
*)

Axiom semAux1 : 
forall   ( P :  Assertion )  lVar  z,
(* (isFresh  z P) -> *) 
evalMyProp ( p_forallsAux   ( fun auxState => 
                                           p_foralls  (fun state => 
                                                 P ( ( stateAtLoop state auxState z lVar )  , auxState))))  ->
 ( forall  aState state, (evalMyProp ( P ( state, aState )))).

(*equivalent assertions have the same truth value for every state*)
Axiom AssertEqEvalEq : forall ass1 ass2  (state: State )( stateAux: AuxState), 
ass1 = ass2 ->  
( evalMyProp ( ass1 (state, stateAux) )) = ( evalMyProp ( ass2 (state, stateAux)) ). 


Lemma semAux2 : forall st stAux ( lVar : list Var) (z : Z), 
 (forall var : Var,
        ~ In var lVar ->
        st var = stateAtLoop st stAux z lVar var) .
Proof with auto.
unfold stateAtLoop.
intros.
destruct In_dec.
contradiction.
trivial.
Qed.

Axiom semAux3 : forall state auxState lVar P z,
( forall var, ( ( ~ In var lVar ) -> (auxState (AuxName var z)) =  (state var)) )->
(evalMyProp (P ( (FreezeVarAtIterStart  state auxState z lVar ) , auxState ) ) )->
( evalMyProp (P (state, auxState))).


Lemma wpModCorrBS: 
forall (stateAux : AuxState  ) ( state1: State) (stmt : stmt_m ) (state2: State) ,
       ( execBs _ state1 stmt  state2) -> 
       forall (pre post : Assertion),   ( wpMod(stmt, post) = pre)  ->
         (evalMyProp ( pre (state1,  stateAux))) -> (evalMyProp (post (state2 , stateAux))).
Proof with auto.
intros stateAux state1 stmt state2 Hexec; induction Hexec; 
intros pre post Hwp;inversion Hwp; subst;intros;auto.
simpl in *.
rewrite <- (progExprEvalProp1 stateAux).
assumption.
(*case skip*)
(*case assignment*)
(*case if  true*)

apply (IHHexec pre_t post);trivial.
simpl in H.
destruct H;auto.
apply H.
rewrite <- (progExprEvalProp1 sAux  stateAux s b).
assumption.
(*case if  false*)
apply (IHHexec pre_f post);trivial.
simpl in H; destruct H;auto.
apply H0.
rewrite <- (progExprEvalProp1 sAux  stateAux s b).
assumption.
(* case while false *)
simpl in H; destruct H.
destruct H0 . 
destruct H1.
destruct H2.
apply H3 ; intros... 
rewrite <- (progExprEvalProp1 sAux  stateAux s b).
assumption.


(*case while true *)

simpl in H; destruct H.
destruct H0.
destruct  H1.
rewrite (progExprEvalProp1 sAux  stateAux s b) in n.
destruct H2.
assert (h := IHHexec1 _ _ H6 (H2 s H0    H n)).
simpl in h.
apply (IHHexec2 _ _ Hwp).
simpl...
repeat split...
intros.
assert(h1 :=  IHHexec1 _ _ H5 ).
assert ( h2 := semAux3 s STAUX  lVar varNotModif z H0 ).
assert ( h3 := h2 (H1 s)). 
forall state auxState lVar P z,
( forall var, ( ( ~ In var lVar ) -> (auxState (AuxName var z)) =  (state var)) )->
(evalMyProp (P ( (FreezeVarAtIterStart  state auxState z lVar ) , auxState ) ) )->
( evalMyProp (P (state, auxState))).

(* case seq *)
apply (IHHexec2 _ _ H4).
apply (IHHexec1 _ _ H5)...
Qed.

 (* Fixpoint  wp_mod (S: Stmt Invariant_m) (post : Assertion) {struct S} : Assertion :=
match S with
| Skip => post
| Affect var expr => (update_assert post var expr) 
| If bExp st1 st2 => let pre_f := (wp_mod st2 post) in
                                 let pre_t := (wp_mod st1 post) in
                                 (fun (s : State ) =>   
                                 	  p_and (p_implies (p_neq ( neval s  bExp  ) 0) (pre_t s))
                                 	   	    (p_implies (p_eq ( neval s  bExp  ) 0)  (pre_f s)))
| While  bExp   (inv_m invariant lVar ) s =>
       let pre_st := (wp_mod s (fun stf => (p_foralls (fun st => 
            p_and
             (p_forallv (fun x => (p_implies (p_not (p_in x lVar)) (p_eq (stf ( progvar x) ) (st (progvar  x) )))))
             (invariant stf)))))
         in 
         (fun st_i : State  => 
             p_and
                (* Initialement l'inv est vrai *)
                (invariant st_i )                    
            (p_and 
               (* la sortie boucle => la poste condition *)
              (p_foralls 
                  (fun st_f => 
                   p_implies
                     (p_forallv (fun x => (p_implies (p_not (p_in x lVar)) (p_eq (st_f (progvar x)) (st_i (progvar x))))))             
                       (p_implies (invariant st_f) (p_implies (p_eq (neval st_f bExp) 0) (post st_f)))))
              (* l'inv est preserve *)
              (p_foralls ( fun st_Si =>
                 (p_implies
                   (p_forallv (fun x => (p_implies (p_not (p_in x lVar)) (p_eq (st_Si (progvar x)) (st_i (progvar x))))))
          	     (p_implies (invariant st_Si) 
                          (p_implies (p_neq (neval st_Si bExp)  0) (pre_st  st_Si) ) )) ))))
	
| Seq st1 st2 => 
                      let pre_st2 := wp_mod st2 post in
                      let pre_st1 := wp_mod st1 pre_st2 in
                      pre_st1
end.
Axiom triche: forall p: Prop, p.

Lemma equiv : 
forall S, forall post pre,  wp_mod S post = pre  <-> wpMod(S, post) ==> pre.
Proof with auto.
intro S. 
induction S; split; intros.

(* Skip1 *)
simpl in H; subst; apply is_wpMod_caseSkip.
(* Skip2 *)
inversion H.
simpl in |- *...

(* Affect 1 *)
simpl in H; subst.
apply  is_wpMod_caseAffect .
(* Affect 2 *)
inversion H.
simpl in |- *...

(* If 1 *)
simpl in H.
rewrite <- H.
apply is_wpMod_caseIf.
assert(h1 := IHS2 post (wp_mod S2 post)); destruct h1.
apply H0...
assert(h1 := IHS1 post (wp_mod S1 post)); destruct h1.
apply H0...

(* If 2 *)
inversion H.
simpl in |- *.
assert(h_1 := IHS1 post pre_t); destruct h_1.
assert(h1 := H8 H6).
rewrite h1.
assert(h_2 := IHS2 post pre_f); destruct h_2.
assert(h2 := H10 H5); subst...

(* While 1 *)
rewrite <- H.
cbv iota delta [wp_mod] beta iota.
(* cbv beta.
cbv iota. *)
case i.
intros a l.
apply is_wpMod_caseWhile.
intros.
simpl.
cbv iota delta [wp_mod] beta iota in IHS.
case i.
simpl in IHS.
intros.
assert (h := IHS 
(fun stf : State =>
p_foralls
  (fun st : State =>
   p_and
     (p_forallv
        (fun x : Var => p_implies (p_not (p_in x l)) (p_eq (stf (progvar x)) (st (progvar x)))))
     (a stf)))
((fix wp_mod (S0 : Stmt Invariant_m) (post0 : Assertion) {struct S0} :
   Assertion :=
   match S0 with
   | Skip => post0
   | Affect var expr => update_assert post0 var expr
   | If bExp st1 st2 =>
       fun s : State =>
       p_and (p_implies (p_neq (neval s bExp) 0) (wp_mod st1 post0 s))
         (p_implies (p_eq (neval s bExp) 0) (wp_mod st2 post0 s))
   | While bExp i0 s =>
       match i0 with
       | inv_m invariant lVar =>
           fun st_i : State =>
           p_and (invariant st_i)
             (p_and
                (p_foralls
                   (fun st_f : State =>
                    p_implies
                      (p_forallv
                         (fun x : Var =>
                          p_implies (p_not (p_in x lVar))
                            (p_eq (st_f (progvar x)) (st_i (progvar x)))))
                      (p_implies (invariant st_f)
                         (p_implies (p_eq (neval st_f bExp) 0) (post0 st_f)))))
                (p_foralls
                   (fun st_Si : State =>
                    p_implies
                      (p_forallv
                         (fun x : Var =>
                          p_implies (p_not (p_in x lVar))
                            (p_eq (st_Si (progvar x)) (st_i (progvar x)))))
                      (p_implies (invariant st_Si)
                         (p_implies (p_neq (neval st_Si bExp) 0)
                            (wp_mod s
                               (fun stf : State =>
                                p_foralls
                                  (fun st : State =>
                                   p_and
                                     (p_forallv
                                        (fun x : Var =>
                                         p_implies (p_not (p_in x lVar))
                                           (p_eq (stf (progvar x)) (st (progvar x)))))
                                     (invariant stf))) st_Si))))))
       end
   | Seq st1 st2 => wp_mod st1 (wp_mod st2 post0)
   end) S
  (fun stf : State =>
   p_foralls
     (fun st : State =>
      p_and
        (p_forallv
           (fun x : Var => p_implies (p_not (p_in x l)) (p_eq (stf (progvar x)) (st (progvar x)))))
        (a stf))))).


destruct h.
intros.
apply H0.
cbv delta [wp_mod].
simpl in  |- *...

(* While 2 *)
inversion H.
simpl.

assert(h := IHS (fun stf : State =>
     p_foralls
       (fun st : State =>
        p_and
          (p_forallv
             (fun x : Var =>
              p_implies (p_not (p_in x lVar)) (p_eq (stf  (progvar x) ) (st (progvar x)))))
          (invariant stf))) pre_st).
destruct h.
assert(h1 := H7 H5).
rewrite h1...

(* Seq 1 *)
simpl in H.
apply is_wpMod_caseSeq with (wp_mod S2 post).
assert(h1 := IHS2 post (wp_mod S2 post)); destruct h1.
apply H0...
assert(h1 := IHS1  (wp_mod S2 post) pre); destruct h1.
apply H0...

(* Seq 2 *)
inversion H.
simpl in |- *.
assert(h_1 := IHS1 pre_st2  pre); destruct h_1.
assert(h1 := H7 H5).
assert(h_2 := IHS2 post pre_st2); destruct h_2.
assert(h2:= H9 H2).
rewrite h2; rewrite h1...
Qed.
*) 