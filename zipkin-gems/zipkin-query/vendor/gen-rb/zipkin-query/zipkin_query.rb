#
# Autogenerated by Thrift Compiler (0.8.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#

require 'thrift'
require 'zipkin_query_types'

    module Zipkin
      module ZipkinQuery
        class Client
          include ::Thrift::Client

          def getTraceIdsBySpanName(service_name, span_name, end_ts, limit, order)
            send_getTraceIdsBySpanName(service_name, span_name, end_ts, limit, order)
            return recv_getTraceIdsBySpanName()
          end

          def send_getTraceIdsBySpanName(service_name, span_name, end_ts, limit, order)
            send_message('getTraceIdsBySpanName', GetTraceIdsBySpanName_args, :service_name => service_name, :span_name => span_name, :end_ts => end_ts, :limit => limit, :order => order)
          end

          def recv_getTraceIdsBySpanName()
            result = receive_message(GetTraceIdsBySpanName_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTraceIdsBySpanName failed: unknown result')
          end

          def getTraceIdsByServiceName(service_name, end_ts, limit, order)
            send_getTraceIdsByServiceName(service_name, end_ts, limit, order)
            return recv_getTraceIdsByServiceName()
          end

          def send_getTraceIdsByServiceName(service_name, end_ts, limit, order)
            send_message('getTraceIdsByServiceName', GetTraceIdsByServiceName_args, :service_name => service_name, :end_ts => end_ts, :limit => limit, :order => order)
          end

          def recv_getTraceIdsByServiceName()
            result = receive_message(GetTraceIdsByServiceName_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTraceIdsByServiceName failed: unknown result')
          end

          def getTraceIdsByAnnotation(service_name, annotation, value, end_ts, limit, order)
            send_getTraceIdsByAnnotation(service_name, annotation, value, end_ts, limit, order)
            return recv_getTraceIdsByAnnotation()
          end

          def send_getTraceIdsByAnnotation(service_name, annotation, value, end_ts, limit, order)
            send_message('getTraceIdsByAnnotation', GetTraceIdsByAnnotation_args, :service_name => service_name, :annotation => annotation, :value => value, :end_ts => end_ts, :limit => limit, :order => order)
          end

          def recv_getTraceIdsByAnnotation()
            result = receive_message(GetTraceIdsByAnnotation_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTraceIdsByAnnotation failed: unknown result')
          end

          def tracesExist(trace_ids)
            send_tracesExist(trace_ids)
            return recv_tracesExist()
          end

          def send_tracesExist(trace_ids)
            send_message('tracesExist', TracesExist_args, :trace_ids => trace_ids)
          end

          def recv_tracesExist()
            result = receive_message(TracesExist_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'tracesExist failed: unknown result')
          end

          def getTracesByIds(trace_ids, adjust)
            send_getTracesByIds(trace_ids, adjust)
            return recv_getTracesByIds()
          end

          def send_getTracesByIds(trace_ids, adjust)
            send_message('getTracesByIds', GetTracesByIds_args, :trace_ids => trace_ids, :adjust => adjust)
          end

          def recv_getTracesByIds()
            result = receive_message(GetTracesByIds_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTracesByIds failed: unknown result')
          end

          def getTraceTimelinesByIds(trace_ids, adjust)
            send_getTraceTimelinesByIds(trace_ids, adjust)
            return recv_getTraceTimelinesByIds()
          end

          def send_getTraceTimelinesByIds(trace_ids, adjust)
            send_message('getTraceTimelinesByIds', GetTraceTimelinesByIds_args, :trace_ids => trace_ids, :adjust => adjust)
          end

          def recv_getTraceTimelinesByIds()
            result = receive_message(GetTraceTimelinesByIds_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTraceTimelinesByIds failed: unknown result')
          end

          def getTraceSummariesByIds(trace_ids, adjust)
            send_getTraceSummariesByIds(trace_ids, adjust)
            return recv_getTraceSummariesByIds()
          end

          def send_getTraceSummariesByIds(trace_ids, adjust)
            send_message('getTraceSummariesByIds', GetTraceSummariesByIds_args, :trace_ids => trace_ids, :adjust => adjust)
          end

          def recv_getTraceSummariesByIds()
            result = receive_message(GetTraceSummariesByIds_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTraceSummariesByIds failed: unknown result')
          end

          def getTraceCombosByIds(trace_ids, adjust)
            send_getTraceCombosByIds(trace_ids, adjust)
            return recv_getTraceCombosByIds()
          end

          def send_getTraceCombosByIds(trace_ids, adjust)
            send_message('getTraceCombosByIds', GetTraceCombosByIds_args, :trace_ids => trace_ids, :adjust => adjust)
          end

          def recv_getTraceCombosByIds()
            result = receive_message(GetTraceCombosByIds_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTraceCombosByIds failed: unknown result')
          end

          def getServiceNames()
            send_getServiceNames()
            return recv_getServiceNames()
          end

          def send_getServiceNames()
            send_message('getServiceNames', GetServiceNames_args)
          end

          def recv_getServiceNames()
            result = receive_message(GetServiceNames_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getServiceNames failed: unknown result')
          end

          def getSpanNames(service_name)
            send_getSpanNames(service_name)
            return recv_getSpanNames()
          end

          def send_getSpanNames(service_name)
            send_message('getSpanNames', GetSpanNames_args, :service_name => service_name)
          end

          def recv_getSpanNames()
            result = receive_message(GetSpanNames_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getSpanNames failed: unknown result')
          end

          def setTraceTimeToLive(trace_id, ttl_seconds)
            send_setTraceTimeToLive(trace_id, ttl_seconds)
            recv_setTraceTimeToLive()
          end

          def send_setTraceTimeToLive(trace_id, ttl_seconds)
            send_message('setTraceTimeToLive', SetTraceTimeToLive_args, :trace_id => trace_id, :ttl_seconds => ttl_seconds)
          end

          def recv_setTraceTimeToLive()
            result = receive_message(SetTraceTimeToLive_result)
            raise result.qe unless result.qe.nil?
            return
          end

          def getTraceTimeToLive(trace_id)
            send_getTraceTimeToLive(trace_id)
            return recv_getTraceTimeToLive()
          end

          def send_getTraceTimeToLive(trace_id)
            send_message('getTraceTimeToLive', GetTraceTimeToLive_args, :trace_id => trace_id)
          end

          def recv_getTraceTimeToLive()
            result = receive_message(GetTraceTimeToLive_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTraceTimeToLive failed: unknown result')
          end

          def getDataTimeToLive()
            send_getDataTimeToLive()
            return recv_getDataTimeToLive()
          end

          def send_getDataTimeToLive()
            send_message('getDataTimeToLive', GetDataTimeToLive_args)
          end

          def recv_getDataTimeToLive()
            result = receive_message(GetDataTimeToLive_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getDataTimeToLive failed: unknown result')
          end

          def getTopAnnotations(service_name)
            send_getTopAnnotations(service_name)
            return recv_getTopAnnotations()
          end

          def send_getTopAnnotations(service_name)
            send_message('getTopAnnotations', GetTopAnnotations_args, :service_name => service_name)
          end

          def recv_getTopAnnotations()
            result = receive_message(GetTopAnnotations_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTopAnnotations failed: unknown result')
          end

          def getTopKeyValueAnnotations(service_name)
            send_getTopKeyValueAnnotations(service_name)
            return recv_getTopKeyValueAnnotations()
          end

          def send_getTopKeyValueAnnotations(service_name)
            send_message('getTopKeyValueAnnotations', GetTopKeyValueAnnotations_args, :service_name => service_name)
          end

          def recv_getTopKeyValueAnnotations()
            result = receive_message(GetTopKeyValueAnnotations_result)
            return result.success unless result.success.nil?
            raise result.qe unless result.qe.nil?
            raise ::Thrift::ApplicationException.new(::Thrift::ApplicationException::MISSING_RESULT, 'getTopKeyValueAnnotations failed: unknown result')
          end

        end

        class Processor
          include ::Thrift::Processor

          def process_getTraceIdsBySpanName(seqid, iprot, oprot)
            args = read_args(iprot, GetTraceIdsBySpanName_args)
            result = GetTraceIdsBySpanName_result.new()
            begin
              result.success = @handler.getTraceIdsBySpanName(args.service_name, args.span_name, args.end_ts, args.limit, args.order)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTraceIdsBySpanName', seqid)
          end

          def process_getTraceIdsByServiceName(seqid, iprot, oprot)
            args = read_args(iprot, GetTraceIdsByServiceName_args)
            result = GetTraceIdsByServiceName_result.new()
            begin
              result.success = @handler.getTraceIdsByServiceName(args.service_name, args.end_ts, args.limit, args.order)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTraceIdsByServiceName', seqid)
          end

          def process_getTraceIdsByAnnotation(seqid, iprot, oprot)
            args = read_args(iprot, GetTraceIdsByAnnotation_args)
            result = GetTraceIdsByAnnotation_result.new()
            begin
              result.success = @handler.getTraceIdsByAnnotation(args.service_name, args.annotation, args.value, args.end_ts, args.limit, args.order)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTraceIdsByAnnotation', seqid)
          end

          def process_tracesExist(seqid, iprot, oprot)
            args = read_args(iprot, TracesExist_args)
            result = TracesExist_result.new()
            begin
              result.success = @handler.tracesExist(args.trace_ids)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'tracesExist', seqid)
          end

          def process_getTracesByIds(seqid, iprot, oprot)
            args = read_args(iprot, GetTracesByIds_args)
            result = GetTracesByIds_result.new()
            begin
              result.success = @handler.getTracesByIds(args.trace_ids, args.adjust)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTracesByIds', seqid)
          end

          def process_getTraceTimelinesByIds(seqid, iprot, oprot)
            args = read_args(iprot, GetTraceTimelinesByIds_args)
            result = GetTraceTimelinesByIds_result.new()
            begin
              result.success = @handler.getTraceTimelinesByIds(args.trace_ids, args.adjust)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTraceTimelinesByIds', seqid)
          end

          def process_getTraceSummariesByIds(seqid, iprot, oprot)
            args = read_args(iprot, GetTraceSummariesByIds_args)
            result = GetTraceSummariesByIds_result.new()
            begin
              result.success = @handler.getTraceSummariesByIds(args.trace_ids, args.adjust)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTraceSummariesByIds', seqid)
          end

          def process_getTraceCombosByIds(seqid, iprot, oprot)
            args = read_args(iprot, GetTraceCombosByIds_args)
            result = GetTraceCombosByIds_result.new()
            begin
              result.success = @handler.getTraceCombosByIds(args.trace_ids, args.adjust)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTraceCombosByIds', seqid)
          end

          def process_getServiceNames(seqid, iprot, oprot)
            args = read_args(iprot, GetServiceNames_args)
            result = GetServiceNames_result.new()
            begin
              result.success = @handler.getServiceNames()
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getServiceNames', seqid)
          end

          def process_getSpanNames(seqid, iprot, oprot)
            args = read_args(iprot, GetSpanNames_args)
            result = GetSpanNames_result.new()
            begin
              result.success = @handler.getSpanNames(args.service_name)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getSpanNames', seqid)
          end

          def process_setTraceTimeToLive(seqid, iprot, oprot)
            args = read_args(iprot, SetTraceTimeToLive_args)
            result = SetTraceTimeToLive_result.new()
            begin
              @handler.setTraceTimeToLive(args.trace_id, args.ttl_seconds)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'setTraceTimeToLive', seqid)
          end

          def process_getTraceTimeToLive(seqid, iprot, oprot)
            args = read_args(iprot, GetTraceTimeToLive_args)
            result = GetTraceTimeToLive_result.new()
            begin
              result.success = @handler.getTraceTimeToLive(args.trace_id)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTraceTimeToLive', seqid)
          end

          def process_getDataTimeToLive(seqid, iprot, oprot)
            args = read_args(iprot, GetDataTimeToLive_args)
            result = GetDataTimeToLive_result.new()
            begin
              result.success = @handler.getDataTimeToLive()
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getDataTimeToLive', seqid)
          end

          def process_getTopAnnotations(seqid, iprot, oprot)
            args = read_args(iprot, GetTopAnnotations_args)
            result = GetTopAnnotations_result.new()
            begin
              result.success = @handler.getTopAnnotations(args.service_name)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTopAnnotations', seqid)
          end

          def process_getTopKeyValueAnnotations(seqid, iprot, oprot)
            args = read_args(iprot, GetTopKeyValueAnnotations_args)
            result = GetTopKeyValueAnnotations_result.new()
            begin
              result.success = @handler.getTopKeyValueAnnotations(args.service_name)
            rescue Zipkin::QueryException => qe
              result.qe = qe
            end
            write_result(result, oprot, 'getTopKeyValueAnnotations', seqid)
          end

        end

        # HELPER FUNCTIONS AND STRUCTURES

        class GetTraceIdsBySpanName_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SERVICE_NAME = 1
          SPAN_NAME = 2
          END_TS = 4
          LIMIT = 5
          ORDER = 6

          FIELDS = {
            SERVICE_NAME => {:type => ::Thrift::Types::STRING, :name => 'service_name'},
            SPAN_NAME => {:type => ::Thrift::Types::STRING, :name => 'span_name'},
            END_TS => {:type => ::Thrift::Types::I64, :name => 'end_ts'},
            LIMIT => {:type => ::Thrift::Types::I32, :name => 'limit'},
            ORDER => {:type => ::Thrift::Types::I32, :name => 'order', :enum_class => Zipkin::Order}
          }

          def struct_fields; FIELDS; end

          def validate
            unless @order.nil? || Zipkin::Order::VALID_VALUES.include?(@order)
              raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Invalid value of field order!')
            end
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceIdsBySpanName_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::LIST, :name => 'success', :element => {:type => ::Thrift::Types::I64}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceIdsByServiceName_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SERVICE_NAME = 1
          END_TS = 3
          LIMIT = 4
          ORDER = 5

          FIELDS = {
            SERVICE_NAME => {:type => ::Thrift::Types::STRING, :name => 'service_name'},
            END_TS => {:type => ::Thrift::Types::I64, :name => 'end_ts'},
            LIMIT => {:type => ::Thrift::Types::I32, :name => 'limit'},
            ORDER => {:type => ::Thrift::Types::I32, :name => 'order', :enum_class => Zipkin::Order}
          }

          def struct_fields; FIELDS; end

          def validate
            unless @order.nil? || Zipkin::Order::VALID_VALUES.include?(@order)
              raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Invalid value of field order!')
            end
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceIdsByServiceName_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::LIST, :name => 'success', :element => {:type => ::Thrift::Types::I64}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceIdsByAnnotation_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SERVICE_NAME = 1
          ANNOTATION = 2
          VALUE = 3
          END_TS = 5
          LIMIT = 6
          ORDER = 7

          FIELDS = {
            SERVICE_NAME => {:type => ::Thrift::Types::STRING, :name => 'service_name'},
            ANNOTATION => {:type => ::Thrift::Types::STRING, :name => 'annotation'},
            VALUE => {:type => ::Thrift::Types::STRING, :name => 'value', :binary => true},
            END_TS => {:type => ::Thrift::Types::I64, :name => 'end_ts'},
            LIMIT => {:type => ::Thrift::Types::I32, :name => 'limit'},
            ORDER => {:type => ::Thrift::Types::I32, :name => 'order', :enum_class => Zipkin::Order}
          }

          def struct_fields; FIELDS; end

          def validate
            unless @order.nil? || Zipkin::Order::VALID_VALUES.include?(@order)
              raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Invalid value of field order!')
            end
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceIdsByAnnotation_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::LIST, :name => 'success', :element => {:type => ::Thrift::Types::I64}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class TracesExist_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          TRACE_IDS = 1

          FIELDS = {
            TRACE_IDS => {:type => ::Thrift::Types::LIST, :name => 'trace_ids', :element => {:type => ::Thrift::Types::I64}}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class TracesExist_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::SET, :name => 'success', :element => {:type => ::Thrift::Types::I64}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTracesByIds_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          TRACE_IDS = 1
          ADJUST = 2

          FIELDS = {
            TRACE_IDS => {:type => ::Thrift::Types::LIST, :name => 'trace_ids', :element => {:type => ::Thrift::Types::I64}},
            ADJUST => {:type => ::Thrift::Types::LIST, :name => 'adjust', :element => {:type => ::Thrift::Types::I32, :enum_class => Zipkin::Adjust}}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTracesByIds_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::LIST, :name => 'success', :element => {:type => ::Thrift::Types::STRUCT, :class => Zipkin::Trace}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceTimelinesByIds_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          TRACE_IDS = 1
          ADJUST = 2

          FIELDS = {
            TRACE_IDS => {:type => ::Thrift::Types::LIST, :name => 'trace_ids', :element => {:type => ::Thrift::Types::I64}},
            ADJUST => {:type => ::Thrift::Types::LIST, :name => 'adjust', :element => {:type => ::Thrift::Types::I32, :enum_class => Zipkin::Adjust}}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceTimelinesByIds_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::LIST, :name => 'success', :element => {:type => ::Thrift::Types::STRUCT, :class => Zipkin::TraceTimeline}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceSummariesByIds_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          TRACE_IDS = 1
          ADJUST = 2

          FIELDS = {
            TRACE_IDS => {:type => ::Thrift::Types::LIST, :name => 'trace_ids', :element => {:type => ::Thrift::Types::I64}},
            ADJUST => {:type => ::Thrift::Types::LIST, :name => 'adjust', :element => {:type => ::Thrift::Types::I32, :enum_class => Zipkin::Adjust}}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceSummariesByIds_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::LIST, :name => 'success', :element => {:type => ::Thrift::Types::STRUCT, :class => Zipkin::TraceSummary}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceCombosByIds_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          TRACE_IDS = 1
          ADJUST = 2

          FIELDS = {
            TRACE_IDS => {:type => ::Thrift::Types::LIST, :name => 'trace_ids', :element => {:type => ::Thrift::Types::I64}},
            ADJUST => {:type => ::Thrift::Types::LIST, :name => 'adjust', :element => {:type => ::Thrift::Types::I32, :enum_class => Zipkin::Adjust}}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceCombosByIds_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::LIST, :name => 'success', :element => {:type => ::Thrift::Types::STRUCT, :class => Zipkin::TraceCombo}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetServiceNames_args
          include ::Thrift::Struct, ::Thrift::Struct_Union

          FIELDS = {

          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetServiceNames_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::SET, :name => 'success', :element => {:type => ::Thrift::Types::STRING}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetSpanNames_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SERVICE_NAME = 1

          FIELDS = {
            SERVICE_NAME => {:type => ::Thrift::Types::STRING, :name => 'service_name'}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetSpanNames_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::SET, :name => 'success', :element => {:type => ::Thrift::Types::STRING}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class SetTraceTimeToLive_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          TRACE_ID = 1
          TTL_SECONDS = 2

          FIELDS = {
            TRACE_ID => {:type => ::Thrift::Types::I64, :name => 'trace_id'},
            TTL_SECONDS => {:type => ::Thrift::Types::I32, :name => 'ttl_seconds'}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class SetTraceTimeToLive_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          QE = 1

          FIELDS = {
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceTimeToLive_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          TRACE_ID = 1

          FIELDS = {
            TRACE_ID => {:type => ::Thrift::Types::I64, :name => 'trace_id'}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTraceTimeToLive_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::I32, :name => 'success'},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetDataTimeToLive_args
          include ::Thrift::Struct, ::Thrift::Struct_Union

          FIELDS = {

          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetDataTimeToLive_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::I32, :name => 'success'},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTopAnnotations_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SERVICE_NAME = 1

          FIELDS = {
            SERVICE_NAME => {:type => ::Thrift::Types::STRING, :name => 'service_name'}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTopAnnotations_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::LIST, :name => 'success', :element => {:type => ::Thrift::Types::STRING}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTopKeyValueAnnotations_args
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SERVICE_NAME = 1

          FIELDS = {
            SERVICE_NAME => {:type => ::Thrift::Types::STRING, :name => 'service_name'}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

        class GetTopKeyValueAnnotations_result
          include ::Thrift::Struct, ::Thrift::Struct_Union
          SUCCESS = 0
          QE = 1

          FIELDS = {
            SUCCESS => {:type => ::Thrift::Types::LIST, :name => 'success', :element => {:type => ::Thrift::Types::STRING}},
            QE => {:type => ::Thrift::Types::STRUCT, :name => 'qe', :class => Zipkin::QueryException}
          }

          def struct_fields; FIELDS; end

          def validate
          end

          ::Thrift::Struct.generate_accessors self
        end

      end

    end
