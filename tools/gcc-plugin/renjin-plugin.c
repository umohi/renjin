/* GCC plugin APIs.

   Copyright (C) 2009, 2010, 2011 Mingjie Xing, mingjie.xing@gmail.com. 

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>. */

#define _GNU_SOURCE

#include <stddef.h>
#include <stdlib.h>
#include <stdarg.h>
#include <stdio.h>
#include <ctype.h>

/* GCC header files.  */


#include "gcc-plugin.h"
#include "plugin.h"
#include "plugin-version.h"


#include "tree.h"
#include "gimple.h"
#include "tree-flow.h"
#include "tree-pass.h"
#include "cfgloop.h"
#include "cgraph.h"
#include "options.h"

/* plugin license check */

int plugin_is_GPL_compatible;


int json_indent_level = 0;
int json_needs_comma = 0;

#define JSON_ARRAY  1
#define JSON_OBJECT  2

typedef struct json_context {
  int needs_comma;
  int type;
  int indent;
} json_context;

// GCC won't let us malloc and I don't want to mess
// around with GCC's internal memory management stuff,
// so we'll just use a fixed-size stack

json_context json_context_stack[128];
int json_context_head = 0;

void json_context_push(int type) {
  json_context_head ++;
  json_context_stack[json_context_head].needs_comma = false;
  json_context_stack[json_context_head].type = type;
}

void json_context_pop() {
  json_context_head--;
}

/* Json writing functions */

void json_indent() {
  int i;
  for(i=0;i!=json_context_head;++i) {
    printf("  ");
  }
}

void json_pre_value() {
  json_context *context = &json_context_stack[json_context_head];
  if(context->type == JSON_ARRAY) {
    if(context->needs_comma) {
      printf(",");
    }
    context->needs_comma = 1;
    printf("\n");
    json_indent(); 
  }
}

void json_start_object() {
  json_pre_value();
  printf("{");
  json_context_push(JSON_OBJECT);
}

void json_start_array() {
  printf("[");
  json_context_push(JSON_ARRAY);
}

void json_null() {
  json_pre_value();
  printf("null");
}

void json_field(const char *name) {
  json_context *context = &json_context_stack[json_context_head];
  if(context->needs_comma) {
    printf(",");
  }
  context->needs_comma = 1;
  printf("\n");
  json_indent();
  printf("\"%s\": ", name);

  json_context_stack[json_context_head].needs_comma = 1;
}

void json_string_field(const char *name, const char *value) {
  json_field(name);
  printf("\"%s\"", value);
}

void json_int(int value) {
  json_pre_value();
  printf("%d", value);
}
void json_int_field(const char *name, int value) {
  json_field(name);
  json_int(value);
}

void json_real_field(const char *name, REAL_VALUE_TYPE value) {
  json_field(name);
  if (REAL_VALUE_ISINF (value)) {
    printf("\"%s\"", REAL_VALUE_NEGATIVE (value) ? "-Inf" : "Inf");
  } else if(REAL_VALUE_ISNAN (value)) {
    printf("\"%s\"", "NaN");
  } else {
    char string[100];
    real_to_decimal (string, &value, sizeof (string), 0, 1);
    printf("%s", string);
  }
}

void json_bool_field(const char *name, int value) {
  json_field(name);
  printf(value ? "true" : "false");
}


void json_array_field(const char *name) {
  json_field(name);
  json_start_array();
}

void json_end_array() {
  printf("\n");
  json_context_pop();
  json_indent();
  printf("]");
}

void json_end_object() {
  json_context_pop();
  printf("\n");
  json_indent();
  printf("}");
}


/* Post pass */


static void dump_type(tree type) {
  json_start_object();
  json_string_field("type", tree_code_name[TREE_CODE(type)]);
    
  if(TYPE_SIZE(type)) {
    json_int_field("size", TREE_INT_CST_LOW(TYPE_SIZE(type)));
  }
  
  switch(TREE_CODE(type)) {
  case INTEGER_TYPE:
    json_int_field("precision", TYPE_PRECISION(type));
    json_bool_field("unsigned", TYPE_UNSIGNED(type));
    break;
  case REAL_TYPE:
    json_int_field("precision", TYPE_PRECISION(type));
    break;
  case POINTER_TYPE:
  case REFERENCE_TYPE:
    json_field("base_type");
    dump_type(TREE_TYPE(type));
    break;

  case ARRAY_TYPE:
    json_field("component_type");
    dump_type(TREE_TYPE(type));
    
    /*if(TYPE_DOMAIN(type)) {
      tree domain = TYPE_DOMAIN(type);
      json_array_field("domain");
      json_int(TREE_INT_CST_LOW(TYPE_MIN_VALUE(domain)));
      json_int(TREE_INT_CST_LOW(TYPE_MAX_VALUE(domain)));
      json_end_array();
    }*/
    break;
  }
  json_end_object();

}

static void dump_op(tree op) {
 	REAL_VALUE_TYPE d;
 	
  if(op) {
    json_start_object();
    json_string_field("expr_type", tree_code_name[TREE_CODE(op)]);
    
   
    switch(TREE_CODE(op)) {
    case FUNCTION_DECL:
    case PARM_DECL:
    case VAR_DECL:
      json_int_field("id", DEBUG_TEMP_UID (op));
      if(DECL_NAME(op)) {
        json_string_field("name", IDENTIFIER_POINTER(DECL_NAME(op)));
      } 
      break;  
      
    case INTEGER_CST:
      json_int_field("value", TREE_INT_CST_LOW (op));
      json_field("type");
      dump_type(TREE_TYPE(op));
      break;
      
    case REAL_CST:
      json_real_field("value", TREE_REAL_CST(op));
      json_field("type");
      dump_type(TREE_TYPE(op));
	    break;
    }
    
    int numops = TREE_OPERAND_LENGTH(op);
    if(numops > 0) {
      json_array_field("operands");
      int i;
      for(i=0;i!=numops;++i) {
         dump_op(TREE_OPERAND(op, i));
      }
      json_end_array();
    }
        
    json_end_object();
  } else {
    json_null();
  }
}

static void dump_ops(gimple stmt) {
  int numops = gimple_num_ops(stmt);
  if(numops > 0) {
    json_array_field("operands");
    int i;
    for(i=0;i<numops;++i) {
      tree op = gimple_op(stmt, i);
      if(op) {
        dump_op(op);
      }
    }
    json_end_array();  
  }
}

static void dump_assignment(gimple stmt) {
  json_string_field("stmt_type", "assignment");

  json_string_field("operator", tree_code_name[gimple_assign_rhs_code(stmt)]);

  json_field("lhs");
  dump_op(gimple_assign_lhs(stmt));
  
  json_array_field("rhs");
  dump_op(gimple_assign_rhs1(stmt));
  dump_op(gimple_assign_rhs2(stmt));
  dump_op(gimple_assign_rhs3(stmt));
  json_end_array();
}


static void dump_cond(basic_block bb, gimple stmt) {
  
  json_string_field("stmt_type", "cond");
  json_string_field("operator", tree_code_name[gimple_assign_rhs_code(stmt)]);
  
  dump_ops(stmt);
      
  edge true_edge, false_edge;
  extract_true_false_edges_from_block (bb, &true_edge, &false_edge);
  
  json_int_field("true_label", true_edge->dest->index);
  json_int_field("false_label", false_edge->dest->index);
  
}

static void dump_goto(gimple stmt) {
  
  json_string_field("stmt_type", "goto");
  
}


static void dump_nop(gimple stmt) {
  
  json_string_field("stmt_type", "nop");
  
}

static void dump_return(gimple stmt) {
  json_string_field("stmt_type", "return");
  
  json_field("retval");
  dump_op(gimple_return_retval(stmt));
}


static void dump_call(gimple stmt) {
  json_string_field("stmt_type", "call");
  
  json_field("lhs");
  dump_op(gimple_call_lhs(stmt));
  
  json_field("fn");
  dump_op(gimple_call_fn(stmt));
  
  int numargs = gimple_call_num_args(stmt);
  if(numargs > 0) {
    json_array_field("args");
    int i;
    for(i=0;i<numargs;++i) {
      dump_op(gimple_call_arg(stmt,i));
    }
    json_end_array();
  }
}


static void dump_statement(basic_block bb, gimple stmt) {

  json_start_object();
  
  switch(gimple_code(stmt)) {
  case GIMPLE_ASSIGN:
    dump_assignment(stmt);
    break;
  case GIMPLE_CALL:
    dump_call(stmt);
    break;
  case GIMPLE_COND:
    dump_cond(bb, stmt);
    break;
  case GIMPLE_GOTO:
    dump_goto(stmt);
    break;
  case GIMPLE_NOP:
    dump_nop(stmt);
    break;
  case GIMPLE_RETURN:
    dump_return(stmt);
    break;
  }
  
  json_end_object();
}


static void dump_argument(tree arg) {
  json_start_object();
  
  json_string_field("name", IDENTIFIER_POINTER(DECL_NAME(arg)));
  json_int_field("id", DEBUG_TEMP_UID (arg));
  
  json_field("type");
  dump_type(TREE_TYPE(arg));
  
  json_end_object();

}

static void dump_arguments(tree decl) {
  
  tree arg = DECL_ARGUMENTS(decl);
  
  if(arg) {
    json_array_field("arguments");
    
    while(arg) {
      dump_argument(arg);
      arg = TREE_CHAIN(arg);
    }  
    
    json_end_array();
  }
}

static void dump_local_decl(tree decl) {

  json_start_object();  
  if(DECL_NAME(decl)) {
    json_string_field("name", IDENTIFIER_POINTER(DECL_NAME(decl)));
  } 
  json_int_field("id", DEBUG_TEMP_UID (decl));
  json_field("type");
  dump_type(TREE_TYPE(decl));
  
  json_end_object();
}

static void dump_local_decls(struct function *fun) {
  unsigned ix;
  tree var;
  
  json_array_field("local_decl");
  
  FOR_EACH_LOCAL_DECL (fun, ix, var)
	  {
	    dump_local_decl(var);
	  }
	json_end_array();
}

static void dump_basic_block(basic_block bb) {

  json_start_object();
  json_int_field("label", bb->index);
  json_array_field("instructions");
      
  gimple_stmt_iterator gsi;
  
  for (gsi = gsi_start_bb (bb); !gsi_end_p (gsi); gsi_next (&gsi))
    {
      dump_statement(bb, gsi_stmt (gsi));
    }
   
  edge e = find_fallthru_edge (bb->succs);

  if (e && e->dest != bb->next_bb)
    {
      json_start_object();
      json_string_field("stmt_type", "goto");
      json_int_field("label", e->dest->index);
      json_end_object();
    }
    
  json_end_array();
  json_end_object();
}

static unsigned int dump_function (void)
{

  basic_block bb;
  
  json_start_object();
  json_int_field("id", DEBUG_TEMP_UID (cfun->decl));
  json_string_field("name", IDENTIFIER_POINTER(DECL_NAME(cfun->decl)));

  dump_arguments(cfun->decl);
  dump_local_decls(cfun);
  
  json_array_field("basic blocks");
  
  FOR_EACH_BB (bb)
    {
      dump_basic_block(bb);  
    }
  
  json_end_array();
  json_end_object();
 
  return 0;
}


static struct gimple_opt_pass pass_dump_json =
{
    {
      GIMPLE_PASS,
      "json", 	/* name */
      NULL,	            /* gate */
      dump_function,	/* execute */
      NULL,		          /* sub */
      NULL,		          /* next */
      0,		            /* static_pass_number */
      0,		            /* tv_id */
      PROP_cfg | PROP_ssa,		/* properties_required */
      0,		/* properties_provided */
      0,		/* properties_destroyed */
      0,		/* todo_flags_start */
      0		/* todo_flags_finish */
    }
};

/* Plugin initialization.  */

int
plugin_init (struct plugin_name_args *plugin_info,
             struct plugin_gcc_version *version)
{
  struct register_pass_info pass_info;
  const char *plugin_name = plugin_info->base_name;
  int argc = plugin_info->argc;
  struct plugin_argument *argv = plugin_info->argv;


  pass_info.pass = &pass_dump_json;
  pass_info.reference_pass_name = "cfg";
  pass_info.ref_pass_instance_number = 1;
  pass_info.pos_op = PASS_POS_INSERT_AFTER;

  /* Register this new pass with GCC */
  register_callback (plugin_name, PLUGIN_PASS_MANAGER_SETUP, NULL,
                     &pass_info);
                     
  //register_callback ("start_unit", PLUGIN_START_UNIT, &start_unit_callback, NULL);
  ///register_callback ("finish_unit", PLUGIN_FINISH_UNIT, &finish_unit_callback, NULL);

  return 0;
}

